/*
 * Copyright 2020 Juan José González Abril
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package videoclub.route

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject
import videoclub.auth.*
import videoclub.db.dao.MemberDao
import videoclub.db.dao.UserDao
import java.nio.ByteBuffer
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.milliseconds

internal fun Route.auth() {
    val userDao by inject<UserDao>()
    val memberDao by inject<MemberDao>()

    route("auth") {
        post("login") {
            val credential = call.receiveOrNull<UserCredential>()
                ?: return@post call.respond(HttpStatusCode.BadRequest)

            if (credential.username.length !in 4..24 || credential.password.length !in 8..128) {
                return@post call.respond(HttpStatusCode.BadRequest)
            }

            val user = userDao.getByCredential(credential)
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

            call.respondAuth(user)
        }

        post("register") {
            val (member, credential) = call.receiveOrNull<RegistrationInfo>()
                ?: return@post call.respond(HttpStatusCode.BadRequest)

            if (credential.username.length !in 4..24 || credential.password.length !in 8..128) {
                return@post call.respond(HttpStatusCode.BadRequest)
            }

            if (userDao.containsUsername(credential.username)) {
                return@post call.respond(HttpStatusCode.Conflict)
            }

            val userId = userDao.add(credential)
                ?: return@post call.respond(HttpStatusCode.InternalServerError)

            memberDao.add(userId, member)
                ?: return@post call.respond(HttpStatusCode.InternalServerError)

            call.respond(HttpStatusCode.OK)
        }

        authenticate {
            get("refresh") {
                val (id) = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                val user = userDao.getById(id)
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)

                call.respondAuth(user)
            }

            get("logout") {
                call.response.cookies.apply {
                    appendExpired(name = AuthConfig.COOKIE_JWT, path = "/")
                    appendExpired(name = AuthConfig.COOKIE_XSRF, path = "/")
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

private suspend fun ApplicationCall.respondAuth(user: User) {
    val xsrfToken = createXsrfToken()

    response.cookies.apply {
        append(jwtCookie(user, xsrfToken))
        append(xsrfCookie(xsrfToken))
    }

    respond(HttpStatusCode.OK, mapOf("lifespan" to AuthConfig.tokenLifespan))
}

// ========== Auxiliary methods ========== //

private val encoder: Base64.Encoder by lazy { Base64.getUrlEncoder().withoutPadding() }
private val cookieMaxAge: Int = AuthConfig.tokenLifespan.milliseconds.toInt(DurationUnit.SECONDS)

private fun createXsrfToken(): String {
    val uuid = UUID.randomUUID()
    val bytes = ByteBuffer.allocate(Long.SIZE_BYTES * 2).apply {
        putLong(uuid.mostSignificantBits)
        putLong(uuid.leastSignificantBits)
    }
    return encoder.encodeToString(bytes.array())
}

private fun jwtCookie(user: User, xsrfToken: String): Cookie = Cookie(
    name = AuthConfig.COOKIE_JWT,
    value = AuthConfig.createToken(user, xsrfToken),
    maxAge = cookieMaxAge,
    path = "/",
    //secure = true,
    httpOnly = true,
    extensions = mapOf("SameSite" to "Strict")
)

private fun xsrfCookie(xsrfToken: String): Cookie = Cookie(
    name = AuthConfig.COOKIE_XSRF,
    value = xsrfToken,
    maxAge = cookieMaxAge,
    path = "/",
    //secure = true,
    extensions = mapOf("SameSite" to "Strict")
)

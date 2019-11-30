/*
 * Copyright 2019 Juan José González Abril
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

import io.ktor.application.call
import io.ktor.auth.UserPasswordCredential
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject
import videoclub.auth.JwtConfig
import videoclub.db.dao.UserDao

fun Route.auth() {
    val userDao by inject<UserDao>()

    route("auth") {
        post("login") {
            val credential = call.receiveOrNull<UserPasswordCredential>()
                ?: return@post call.respond(HttpStatusCode.BadRequest)

            if (credential.name.length !in 4..24 || credential.password.length !in 8..128) {
                return@post call.respond(HttpStatusCode.BadRequest)
            }

            val user = userDao.getByCredential(credential)
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

            val token = JwtConfig.createToken(user)

            call.respond(mapOf("token" to token))
        }

        post("register") {
            val credential = call.receiveOrNull<UserPasswordCredential>()
                ?: return@post call.respond(HttpStatusCode.BadRequest)

            if (credential.name.length !in 4..24 || credential.password.length !in 8..128) {
                return@post call.respond(HttpStatusCode.BadRequest)
            }

            if (userDao.containsUsername(credential.name)) {
                return@post call.respond(HttpStatusCode.Conflict)
            }

            val success = userDao.add(credential)

            when {
                success -> call.respond(HttpStatusCode.OK)
                else -> call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

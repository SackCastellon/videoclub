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

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.route
import org.koin.ktor.ext.inject
import videoclub.auth.User.Type.ADMIN
import videoclub.auth.User.Type.MEMBER
import videoclub.auth.UserPrincipal
import videoclub.data.Admin
import videoclub.data.Member
import videoclub.db.dao.AdminDao
import videoclub.db.dao.MemberDao
import videoclub.db.dao.UserDao

internal fun Route.users() {
    val userDao by inject<UserDao>()
    val memberDao by inject<MemberDao>()
    val adminDao by inject<AdminDao>()

    authenticate {
        route("user") {
            get {
                val (userId, userType) = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                val user = userDao.getById(userId)
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                when (userType) {
                    MEMBER -> {
                        val member = memberDao.getById(userId)
                            ?: return@get call.respond(HttpStatusCode.Forbidden)

                        call.respond(
                            HttpStatusCode.OK, mapOf(
                                "user" to user,
                                "member" to member
                            )
                        )
                    }
                    ADMIN -> {
                        val admin = adminDao.getById(userId)
                            ?: return@get call.respond(HttpStatusCode.Forbidden)

                        call.respond(
                            HttpStatusCode.OK, mapOf(
                                "user" to user,
                                "admin" to admin
                            )
                        )
                    }
                }
            }

            patch {
                val (userId, userType) = call.principal<UserPrincipal>()
                    ?: return@patch call.respond(HttpStatusCode.Forbidden)

                when (userType) {
                    MEMBER -> {
                        val memberUpdate = call.receiveOrNull<Member.Update>()
                            ?: return@patch call.respond(HttpStatusCode.BadRequest)

                        val success = memberDao.update(userId, memberUpdate)

                        if (success) {
                            call.respond(HttpStatusCode.NoContent)
                        } else {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                    ADMIN -> {
                        val adminUpdate = call.receiveOrNull<Admin.Update>()
                            ?: return@patch call.respond(HttpStatusCode.BadRequest)

                        val success = adminDao.update(userId, adminUpdate)

                        if (success) {
                            call.respond(HttpStatusCode.NoContent)
                        } else {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                }
            }
        }
    }
}

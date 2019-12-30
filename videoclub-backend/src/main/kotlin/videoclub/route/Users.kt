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
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.koin.ktor.ext.inject
import videoclub.auth.UserPrincipal
import videoclub.db.dao.AdminDao
import videoclub.db.dao.MemberDao
import videoclub.db.dao.UserDao

fun Route.users() {
    val userDao by inject<UserDao>()
    val memberDao by inject<MemberDao>()
    val adminDao by inject<AdminDao>()

    authenticate {
        route("user") {
            get {
                val (id) = call.authentication.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                val user = userDao.getById(id)
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                call.respond(user)
            }
        }

        route("member") {
            get {
                val (id) = call.authentication.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                val member = memberDao.getById(id)
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                call.respond(member)
            }
        }

        route("admin") {
            get {
                val (id) = call.authentication.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                val admin = adminDao.getById(id)
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                call.respond(admin)
            }
        }
    }
}

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
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import videoclub.auth.User
import videoclub.auth.UserPrincipal
import videoclub.data.Rental
import videoclub.db.dao.RentalDao

internal fun Route.rentals() {
    val rentalDao by inject<RentalDao>()

    authenticate {
        route("rental") {
            get("{id}") {
                val (userId, type) = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                if (type != User.Type.MEMBER) {
                    return@get call.respond(HttpStatusCode.Forbidden)
                }

                val rentalId = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                val rental = rentalDao.getById(rentalId)
                    ?: return@get call.respond(HttpStatusCode.NotFound)

                if (rental.memberId != userId) {
                    return@get call.respond(HttpStatusCode.Forbidden)
                }

                call.respond(HttpStatusCode.OK, rental)
            }

            post {
                val (userId, type) = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Forbidden)

                if (type != User.Type.MEMBER) {
                    return@post call.respond(HttpStatusCode.Forbidden)
                }

                val newRental = call.receiveOrNull<Rental.New>()
                    ?: return@post call.respond(HttpStatusCode.BadRequest)

                val rentalId = rentalDao.add(userId, newRental)
                    ?: return@post call.respond(HttpStatusCode.InternalServerError)

                call.respond(HttpStatusCode.Created, mapOf("rentalId" to rentalId))
            }

            patch("{id}") {
                val (userId, type) = call.principal<UserPrincipal>()
                    ?: return@patch call.respond(HttpStatusCode.Forbidden)

                if (type != User.Type.MEMBER) {
                    return@patch call.respond(HttpStatusCode.Forbidden)
                }

                val rentalId = call.parameters["id"]?.toIntOrNull()
                    ?: return@patch call.respond(HttpStatusCode.BadRequest)

                if (!rentalDao.isOwner(memberId = userId, rentalId = rentalId)) {
                    return@patch call.respond(HttpStatusCode.Forbidden)
                }

                val rentalUpdate = call.receiveOrNull<Rental.Update>()
                    ?: return@patch call.respond(HttpStatusCode.BadRequest)

                val success = rentalDao.update(rentalId, rentalUpdate)

                if (success) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}

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
import videoclub.data.Movie
import videoclub.db.dao.MovieDao
import videoclub.db.dao.ShopDao

internal fun Route.movies() {
    val movieDao by inject<MovieDao>()
    val shopDao by inject<ShopDao>()

    route("movie") {
        get {
            val movies = movieDao.getAll()

            call.respond(HttpStatusCode.OK, movies)
        }

        get("new") {
            val movies = movieDao.getNewest()

            call.respond(HttpStatusCode.OK, movies)
        }

        get("top") {
            val movies = movieDao.getPopular()

            call.respond(HttpStatusCode.OK, movies)
        }

        get("rental/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            val movie = movieDao.getByRentalId(id)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(HttpStatusCode.OK, movie)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            val movie = movieDao.getById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(HttpStatusCode.OK, movie)
        }

        authenticate {
            post {
                val (_, type) = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Forbidden)

                if (type != User.Type.ADMIN) {
                    return@post call.respond(HttpStatusCode.Forbidden)
                }

                val movie = call.receiveOrNull<Movie.New>()
                    ?: return@post call.respond(HttpStatusCode.BadRequest)

                if (!shopDao.containsId(movie.shopId)) {
                    return@post call.respond(HttpStatusCode.NotFound)
                }

                val movieId = movieDao.add(movie)
                    ?: return@post call.respond(HttpStatusCode.InternalServerError)

                call.respond(HttpStatusCode.Created, mapOf("movieId" to movieId))
            }

            patch("{id}") {
                val (_, type) = call.principal<UserPrincipal>()
                    ?: return@patch call.respond(HttpStatusCode.Forbidden)

                if (type != User.Type.ADMIN) {
                    return@patch call.respond(HttpStatusCode.Forbidden)
                }

                val movieId = call.parameters["id"]?.toIntOrNull()
                    ?: return@patch call.respond(HttpStatusCode.BadRequest)

                val movieUpdate = call.receiveOrNull<Movie.Update>()
                    ?: return@patch call.respond(HttpStatusCode.BadRequest)

                val success = movieDao.update(movieId, movieUpdate)

                if (success) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}

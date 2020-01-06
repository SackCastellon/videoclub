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

package videoclub.db.dao

import videoclub.data.Movie

/**
 * DAO for [Movie]
 */
interface MovieDao {

    /**
     * Returns the number of movies.
     */
    suspend fun count(): Int

    /**
     * Returns a list with all the movies.
     */
    suspend fun getAll(): List<Movie>

    /**
     * Returns a list with the ten newest movies.
     */
    suspend fun getNewest(): List<Movie>

    /**
     * Returns a list with the ten most popular movies.
     */
    suspend fun getPopular(): List<Movie>

    /**
     * Returns the movie with the given [id], if any.
     */
    suspend fun getById(id: Int): Movie?

    /**
     * Adds a new [movie].
     * Returns the id of the new movie if it was added succesfully, `null` otherwise.
     */
    suspend fun add(movie: Movie.New): Int?

    /**
     * Updates the information of the [Movie] with the given [id].
     * Returns `true` if the update was successful, `false` otherwise.
     */
    suspend fun update(id: Int, movie: Movie.Update): Boolean
}

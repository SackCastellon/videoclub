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

import org.jetbrains.exposed.sql.*
import videoclub.data.Movie
import videoclub.data.MovieUpdate
import videoclub.db.DatabaseConfig.dbQuery
import videoclub.db.sql.tables.Movies

internal object MovieDaoImpl : MovieDao {

    override suspend fun count(): Int = dbQuery {
        Movies.selectAll().count()
    }

    override suspend fun getAll(): List<Movie> = dbQuery {
        Movies.selectAll().orderBy(Movies.name to SortOrder.ASC).map(::toMovie)
    }

    override suspend fun getNewest(): List<Movie> = dbQuery {
        Movies.selectAll().orderBy(Movies.releaseDate to SortOrder.DESC).limit(10).map(::toMovie)
    }

    override suspend fun getPopular(): List<Movie> = dbQuery {
        emptyList<Movie>() // TODO
    }

    override suspend fun getById(id: Int): Movie? = dbQuery {
        Movies.select { Movies.id eq id }.mapLazy(::toMovie).singleOrNull()
    }

    override suspend fun add(movie: MovieUpdate): Int? = dbQuery {
        Movies.insert {
            it[shopId] = movie.shopId
            it[name] = movie.name
            it[director] = movie.director
            it[releaseDate] = movie.releaseDate
            it[price] = movie.price
        }.getOrNull(Movies.id)
    }

    private fun toMovie(it: ResultRow) = Movie(
        id = it[Movies.id],
        shopId = it[Movies.shopId],
        name = it[Movies.name],
        director = it[Movies.director],
        releaseDate = it[Movies.releaseDate],
        price = it[Movies.price]
    )
}

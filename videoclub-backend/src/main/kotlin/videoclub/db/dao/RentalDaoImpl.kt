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
import videoclub.data.Rental
import videoclub.db.DatabaseConfig.dbQuery
import videoclub.db.sql.tables.Movies
import videoclub.db.sql.tables.RentalMovies
import videoclub.db.sql.tables.Rentals

internal object RentalDaoImpl : RentalDao {

    private val priceSum = Movies.price.sum()

    override suspend fun count(): Int = dbQuery {
        Rentals.selectAll().count()
    }

    override suspend fun isOwner(memberId: Int, rentalId: Int): Boolean = dbQuery {
        Rentals.select { (Rentals.id eq rentalId) and (Rentals.memberId eq memberId) }.empty().not()
    }

    override suspend fun getById(id: Int): Rental? = dbQuery {
        Rentals.innerJoin(RentalMovies).slice(Rentals.columns + RentalMovies.movieId)
            .select { Rentals.id eq id }
            .groupBy { it[Rentals.id] }
            .values
            .singleOrNull()
            ?.let { rows ->
                val row = rows.first()
                Rental(
                    id = row[Rentals.id],
                    memberId = row[Rentals.memberId],
                    movieIds = rows.map { it[RentalMovies.movieId] }.toSet(),
                    pickupDate = row[Rentals.pickupDate],
                    returnDate = row[Rentals.returnDate],
                    cost = row[Rentals.cost]
                )
            }
    }

    override suspend fun getByMember(memberId: Int): List<Rental> = dbQuery {
        Rentals.innerJoin(RentalMovies).slice(Rentals.columns + RentalMovies.movieId)
            .select { Rentals.memberId eq memberId }
            .groupBy { it[Rentals.id] }
            .values
            .map { rows ->
                val row = rows.first()
                Rental(
                    id = row[Rentals.id],
                    memberId = row[Rentals.memberId],
                    movieIds = rows.map { it[RentalMovies.movieId] }.toSet(),
                    pickupDate = row[Rentals.pickupDate],
                    returnDate = row[Rentals.returnDate],
                    cost = row[Rentals.cost]
                )
            }
    }

    override suspend fun add(memberId: Int, rental: Rental.New): Int? = dbQuery {
        val total = Movies.slice(priceSum)
            .select { Movies.id inList rental.movieIds }
            .mapLazy { it[priceSum] }
            .singleOrNull()
            ?: return@dbQuery null

        val id = Rentals.insert {
            it[this.memberId] = memberId
            it[pickupDate] = rental.pickupDate
            it[returnDate] = rental.returnDate
            it[cost] = total
        }.getOrNull(Rentals.id)
            ?: return@dbQuery null

        RentalMovies.batchInsert(rental.movieIds) { movieId ->
            this[RentalMovies.rentalId] = id
            this[RentalMovies.movieId] = movieId
        }

        id
    }

    override suspend fun update(id: Int, rental: Rental.Update): Boolean = dbQuery {
        val update = Rentals.update({ Rentals.id eq id }) {
            it[returnDate] = rental.returnDate
        }

        update > 0
    }
}

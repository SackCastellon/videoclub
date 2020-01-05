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

package videoclub.db.sql.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.math.BigDecimal

internal object Rentals : Table() {
    val id = integer("id").autoIncrement()
    val memberId = integer("member_id") references Members.id
    val pickupDate = datetime("pickup_date")
    val returnDate = datetime("return_date")
    val cost = decimal("cost", 8, 2).check { it greater BigDecimal.ZERO }

    override val primaryKey = PrimaryKey(id)
}

internal object RentalMovies : Table() {
    val rentalId = integer("rental_id") references Rentals.id
    val movieId = integer("movie_id") references Movies.id

    override val primaryKey = PrimaryKey(rentalId, movieId)
}

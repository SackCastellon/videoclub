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

package videoclub.data

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Information about a movie rental.
 */
data class Rental(
    /** A unique id for this rental. */
    val id: Int,
    /** The member that rented the movies */
    val memberId: Int,
    /** The set of movies being rented. */
    val movieIds: Set<Int>,
    /** The date when the movie was picked up. */
    val pickupDate: LocalDateTime,
    /** The date when the movie will be returned. */
    val returnDate: LocalDateTime,
    /** The total cost of the rental. */
    val cost: BigDecimal
) {
    /**
     * New information about a movie rental.
     */
    data class New(
        /** The set of movies being rented. */
        val movieIds: Set<Int>,
        /** The date when the movie was picked up. */
        val pickupDate: LocalDateTime,
        /** The date when the movie will be returned. */
        val returnDate: LocalDateTime
    )

    /**
     * An update of the information about a movie rental.
     */
    data class Update(
        /** The updated date when the movie will be returned. */
        val returnDate: LocalDateTime
    )
}

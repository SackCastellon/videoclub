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
import videoclub.data.Rental

/**
 * DAO for [Movie]
 */
interface RentalDao {

    /**
     * Returns the number of rentals.
     */
    suspend fun count(): Int

    /**
     * Returns `true` if the member with the given [memberId] is the owner of the rental with the given [rentalId]
     */
    suspend fun isOwner(memberId: Int, rentalId: Int): Boolean

    /**
     * Returns the rental with the given [id], if any.
     */
    suspend fun getById(id: Int): Rental?

    /**
     * Adds information about a new [Rental].
     * Returns the id of the new rental if it was added succesfully, `null` otherwise.
     */
    suspend fun add(memberId: Int, rental: Rental.New): Int?

    /**
     * Updates the information of the [Rental] with the given [rentalId].
     * Returns `true` if the update was successful, `false` otherwise.
     */
    suspend fun update(rentalId: Int, rental: Rental.Update): Boolean
}

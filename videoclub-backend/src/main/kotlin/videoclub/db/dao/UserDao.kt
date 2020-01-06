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

import videoclub.auth.User
import videoclub.auth.UserCredential
import videoclub.data.Member

/**
 * DAO for [Member].
 */
interface UserDao {

    /**
     * Returns the number of users.
     */
    suspend fun count(): Int

    /**
     * Returns the user with the given [id], if any.
     */
    suspend fun getById(id: Int): User?

    /**
     * Returns the id of the user that matches the given [credential], if any.
     */
    suspend fun getByCredential(credential: UserCredential): User?

    /**
     * Returns `true` if the given [id] is already stored, `false` otherwise.
     */
    suspend fun containsId(id: Int): Boolean

    /**
     * Returns `true` if the given [username] is already stored, `false` otherwise.
     */
    suspend fun containsUsername(username: String): Boolean

    /**
     * Adds a new [user].
     * Returns the id of the new user if it was added succesfully, `null` otherwise.
     */
    suspend fun add(user: UserCredential): Int?
}

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

package videoclub.db.dao

import videoclub.auth.UserCredential
import videoclub.auth.RegistrationInfo
import videoclub.data.Member

/**
 * DAO for [Member].
 */
interface MemberDao {

    /**
     * Returns the id of the member that matches the given [credential], if any.
     */
    suspend fun findId(credential: UserCredential): Int?

    /**
     * Returns the member with the given [id], if any.
     */
    suspend fun getById(id: Int): Member?

    /**
     * Returns the member with the given [username], if any.
     */
    suspend fun getByUsername(username: String): Member?

    /**
     * Returns `true` if the given [id] is already stored, `false` otherwise.
     */
    suspend fun containsId(id: Int): Boolean

    /**
     * Returns `true` if the given [username] is already stored, `false` otherwise.
     */
    suspend fun containsUsername(username: String): Boolean

    /**
     * Adds a new member using the given [info].
     * Returns `true` if the member was added successfully, `false` otherwise.
     */
    suspend fun add(info: RegistrationInfo): Boolean
}

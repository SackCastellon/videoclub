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

import videoclub.data.Member

/**
 * DAO for [Member].
 */
interface MemberDao {

    /**
     * Returns the number of members.
     */
    suspend fun count(): Int

    /**
     * Returns the member with the given [id], if any.
     */
    suspend fun getById(id: Int): Member?

    /**
     * Returns `true` if the given [id] is already stored, `false` otherwise.
     */
    suspend fun containsId(id: Int): Boolean

    /**
     * Adds a new [member] with the given [userId].
     * Returns the id of the new member if it was added succesfully, `null` otherwise.
     */
    suspend fun add(userId: Int, member: Member.New): Int?
}

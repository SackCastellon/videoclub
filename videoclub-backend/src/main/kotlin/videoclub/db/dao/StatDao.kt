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

import videoclub.data.Stat

/**
 * DAO for [Stat]
 */
interface StatDao {

    /**
     * Returns a list with all the stats.
     */
    suspend fun getAll(): List<Stat>

    /**
     * Returns the stat with the given [id], if any.
     */
    suspend fun getById(id: Int): Stat?

    /**
     * Adds a new [stat].
     * Returns the id of the new stat if it was added succesfully, `null` otherwise.
     */
    suspend fun add(stat: Stat.New): Int?
}

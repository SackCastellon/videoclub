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
import org.jetbrains.exposed.sql.`java-time`.month
import org.jetbrains.exposed.sql.`java-time`.year
import videoclub.data.Stat
import videoclub.db.DatabaseConfig.dbQuery
import videoclub.db.sql.tables.Rentals
import videoclub.db.sql.tables.Stats
import java.math.BigDecimal

internal object StatDaoImpl : StatDao {

    private val costSum = Rentals.cost.sum()

    override suspend fun getAll(): List<Stat> = dbQuery {
        Stats.selectAll().map(::toStat)
    }

    override suspend fun getById(id: Int): Stat? = dbQuery {
        Stats.select { Stats.id eq id }.mapLazy(::toStat).singleOrNull()
    }

    override suspend fun add(stat: Stat.New): Int? = dbQuery {
        val total = Rentals.slice(costSum)
            .select {
                (Rentals.memberId eq stat.memberId) and
                        (Rentals.pickupDate.month() eq stat.creationDate.monthValue) and
                        (Rentals.pickupDate.year() eq stat.creationDate.year)
            }
            .mapLazy { it[costSum] }
            .singleOrNull() ?: BigDecimal.ZERO

        Stats.insert {
            it[memberId] = stat.memberId
            it[creationDate] = stat.creationDate.withDayOfMonth(1)
            it[totalSpent] = total
        }.getOrNull(Stats.id)
    }

    private fun toStat(it: ResultRow) = Stat(
        id = it[Stats.id],
        memberId = it[Stats.memberId],
        creationDate = it[Stats.creationDate],
        totalSpent = it[Stats.totalSpent]
    )
}

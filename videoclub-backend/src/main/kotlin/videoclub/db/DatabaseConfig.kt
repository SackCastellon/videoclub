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

package videoclub.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.KoinComponent
import org.koin.core.inject
import videoclub.auth.UserCredential
import videoclub.data.AdminUpdate
import videoclub.db.dao.AdminDao
import videoclub.db.dao.UserDao
import videoclub.db.sql.tables.*

internal object DatabaseConfig : KoinComponent {

    private val db by lazy { Database.connect(hikari()) }

    private fun hikari(): HikariDataSource {
        return HikariConfig().apply {
            jdbcUrl = System.getenv("DATABASE_URL").let(::checkNotNull)
            username = System.getenv("DATABASE_USER").let(::checkNotNull)
            password = System.getenv("DATABASE_PASSWORD").let(::checkNotNull)
            maximumPoolSize = 5
            isAutoCommit = false
        }.also(HikariConfig::validate).let(::HikariDataSource)
    }

    fun setup(): Unit = runBlocking {
        dbQuery {
            SchemaUtils.create(
                Users,
                Shops,
                Movies,
                Members,
                Rentals,
                MemberMovieRental,
                Stats,
                Admins,
                inBatch = true
            )

            val userDao by inject<UserDao>()
            val adminDao by inject<AdminDao>()

            if (adminDao.count() == 0) {
                val userId = userDao.add(UserCredential("admin", "adminpasswd"))
                checkNotNull(userId) { "Failed to add user 'admin'" }
                val adminId = adminDao.add(userId, AdminUpdate("Administrator"))
                checkNotNull(adminId) { "Failed to add admin 'Administrator'" }
            }
        }
    }

    suspend fun <T> dbQuery(statement: suspend Transaction.() -> T): T =
        newSuspendedTransaction(Dispatchers.IO, db, statement)
}

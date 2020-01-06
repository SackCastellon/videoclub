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
import org.koin.core.KoinComponent
import org.koin.core.inject
import videoclub.data.Admin
import videoclub.db.DatabaseConfig.dbQuery
import videoclub.db.sql.tables.Admins

internal object AdminDaoImpl : AdminDao, KoinComponent {

    private val userDao by inject<UserDao>()
    private val memberDao by inject<MemberDao>()

    override suspend fun count(): Int = dbQuery {
        Admins.selectAll().count()
    }

    override suspend fun getById(id: Int): Admin? = dbQuery {
        Admins.select { Admins.id eq id }.mapLazy(::toAdmin).singleOrNull()
    }

    override suspend fun containsId(id: Int): Boolean = dbQuery {
        Admins.select { Admins.id eq id }.empty().not()
    }

    override suspend fun add(userId: Int, admin: Admin.New): Int? = dbQuery {
        if (!userDao.containsId(userId)) return@dbQuery null
        if (memberDao.containsId(userId)) return@dbQuery null

        Admins.insert {
            it[id] = userId
            it[name] = admin.name
        }.getOrNull(Admins.id)
    }

    override suspend fun update(userId: Int, admin: Admin.Update): Boolean = dbQuery {
        val update = Admins.update({ Admins.id eq userId }) {
            it[name] = admin.name
        }

        update > 0
    }

    private fun toAdmin(it: ResultRow) = Admin(
        id = it[Admins.id],
        name = it[Admins.name]
    )
}

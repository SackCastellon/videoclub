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
import videoclub.data.Member
import videoclub.db.DatabaseConfig.dbQuery
import videoclub.db.sql.tables.Members

internal object MemberDaoImpl : MemberDao, KoinComponent {

    private val userDao by inject<UserDao>()
    private val adminDao by inject<AdminDao>()

    override suspend fun count(): Int = dbQuery {
        Members.selectAll().count()
    }

    override suspend fun getById(id: Int): Member? = dbQuery {
        Members.select { Members.id eq id }.mapLazy(::toMember).singleOrNull()
    }

    override suspend fun containsId(id: Int): Boolean = dbQuery {
        Members.select { Members.id eq id }.empty().not()
    }

    override suspend fun add(userId: Int, member: Member.New): Int? = dbQuery {
        if (!userDao.containsId(userId)) return@dbQuery null
        if (adminDao.containsId(userId)) return@dbQuery null

        Members.insert {
            it[id] = userId
            it[name] = member.name
            it[age] = member.age
        }.getOrNull(Members.id)
    }

    override suspend fun update(userId: Int, member: Member.Update): Boolean = dbQuery {
        val update = Members.update({ Members.id eq userId }) {
            it[name] = member.name
            it[age] = member.age
        }

        update > 0
    }

    private fun toMember(it: ResultRow) = Member(
        id = it[Members.id],
        name = it[Members.name],
        age = it[Members.age]
    )
}

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

import org.jetbrains.exposed.sql.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.springframework.security.crypto.password.PasswordEncoder
import videoclub.auth.UserCredential
import videoclub.auth.RegistrationInfo
import videoclub.data.Member
import videoclub.db.DatabaseConfig.dbQuery
import videoclub.db.sql.tables.Members

internal object MemberDaoImpl : MemberDao, KoinComponent {

    private val encoder by inject<PasswordEncoder>()
    private val publicColumns: List<Column<*>> = Members.columns - Members.password

    override suspend fun findId(credential: UserCredential): Int? = dbQuery {
        val (id, encodedPassword) = Members.slice(Members.id, Members.password)
            .select { Members.username eq credential.username }
            .mapLazy { it[Members.id] to it[Members.password] }
            .singleOrNull() ?: return@dbQuery null

        if (!encoder.matches(credential.password, encodedPassword)) return@dbQuery null

        id
    }

    override suspend fun getById(id: Int): Member? = dbQuery {
        Members.slice(publicColumns).select { Members.id eq id }.mapLazy(::toMember).singleOrNull()
    }

    override suspend fun getByUsername(username: String): Member? = dbQuery {
        Members.slice(publicColumns).select { Members.username eq username }.mapLazy(::toMember).singleOrNull()
    }

    override suspend fun containsId(id: Int): Boolean = dbQuery {
        Members.select { Members.id eq id }.empty().not()
    }

    override suspend fun containsUsername(username: String): Boolean = dbQuery {
        Members.select { Members.username eq username }.empty().not()
    }

    override suspend fun add(info: RegistrationInfo): Boolean = dbQuery{
        val encodedPassword = encoder.encode(info.password)

        val id = Members.insert {
            it[name] = info.name
            it[age] = info.age
            it[username] = info.username
            it[password] = encodedPassword
        }.getOrNull(Members.id)

        id != null
    }

    private fun toMember(it: ResultRow) = Member(
        id = it[Members.id],
        name = it[Members.name],
        age = it[Members.age],
        username = it[Members.username]
    )
}

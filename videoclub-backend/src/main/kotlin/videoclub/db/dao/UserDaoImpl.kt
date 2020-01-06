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
import org.springframework.security.crypto.password.PasswordEncoder
import videoclub.auth.User
import videoclub.auth.UserCredential
import videoclub.db.DatabaseConfig.dbQuery
import videoclub.db.sql.tables.Admins
import videoclub.db.sql.tables.Members
import videoclub.db.sql.tables.Users

internal object UserDaoImpl : UserDao, KoinComponent {

    private val encoder by inject<PasswordEncoder>()
    private val publicColumns: List<Column<*>> = Users.columns - Users.password


    override suspend fun count(): Int = dbQuery {
        Users.selectAll().count()
    }

    override suspend fun getByCredential(credential: UserCredential): User? = dbQuery {
        val (id, username, encodedPassword) = Users
            .select { Users.username eq credential.username }
            .mapLazy { Triple(it[Users.id], it[Users.username], it[Users.password]) }
            .singleOrNull() ?: return@dbQuery null

        if (!encoder.matches(credential.password, encodedPassword)) return@dbQuery null

        val type = findUserType(id) ?: return@dbQuery null

        User(id, username, type)
    }

    override suspend fun getById(id: Int): User? = dbQuery {
        val type = findUserType(id) ?: return@dbQuery null

        Users.slice(publicColumns).select { Users.id eq id }.mapLazy {
            User(
                id = it[Users.id],
                username = it[Users.username],
                type = type
            )
        }.singleOrNull()
    }

    override suspend fun containsId(id: Int): Boolean = dbQuery {
        Users.select { Users.id eq id }.empty().not()
    }

    override suspend fun containsUsername(username: String): Boolean = dbQuery {
        Users.select { Users.username eq username }.empty().not()
    }

    override suspend fun add(user: UserCredential): Int? = dbQuery {
        val encodedPassword = encoder.encode(user.password)

        Users.insert {
            it[username] = user.username
            it[password] = encodedPassword
        }.getOrNull(Users.id)
    }

    private fun findUserType(id: Int): User.Type? = when {
        Members.select { Members.id eq id }.empty().not() -> User.Type.MEMBER
        Admins.select { Admins.id eq id }.empty().not() -> User.Type.ADMIN
        else -> null
    }
}

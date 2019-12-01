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

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.mapLazy
import org.jetbrains.exposed.sql.select
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.springframework.security.crypto.password.PasswordEncoder
import videoclub.auth.User
import videoclub.auth.UserCredential
import videoclub.db.DatabaseConfig.dbQuery
import videoclub.db.sql.tables.Users

internal object UserDaoImpl : UserDao, KoinComponent {

    private val encoder by inject<PasswordEncoder>()

    override suspend fun getById(id: Int): User? = dbQuery {
        Users.select { Users.id eq id }.mapLazy { User(it[Users.id], it[Users.username]) }.singleOrNull()
    }

    override suspend fun getByCredential(credential: UserCredential): User? = dbQuery {
        val (id, encodedPassword) = Users.slice(Users.id, Users.password)
            .select { Users.username eq credential.username }
            .mapLazy { it[Users.id] to it[Users.password] }
            .singleOrNull() ?: return@dbQuery null

        if (!encoder.matches(credential.password, encodedPassword)) return@dbQuery null

        User(id, credential.username)
    }

    override suspend fun containsUsername(username: String): Boolean = dbQuery {
        Users.select { Users.username eq username }.empty().not()
    }

    override suspend fun add(credential: UserCredential): Boolean = dbQuery {
        val encodedPassword = encoder.encode(credential.password)

        val id = Users.insert {
            it[username] = credential.username
            it[password] = encodedPassword
        }.getOrNull(Users.id)

        id != null
    }
}

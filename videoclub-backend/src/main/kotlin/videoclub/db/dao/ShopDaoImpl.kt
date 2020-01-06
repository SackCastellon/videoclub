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
import videoclub.data.Shop
import videoclub.db.DatabaseConfig.dbQuery
import videoclub.db.sql.tables.Shops

internal object ShopDaoImpl : ShopDao {

    override suspend fun count(): Int = dbQuery {
        Shops.selectAll().count()
    }

    override suspend fun containsId(id: Int): Boolean = dbQuery {
        Shops.select { Shops.id eq id }.empty().not()
    }

    override suspend fun getAll(): List<Shop> = dbQuery {
        Shops.selectAll().map(::toShop)
    }

    override suspend fun getById(id: Int): Shop? = dbQuery {
        Shops.select { Shops.id eq id }.mapLazy(::toShop).singleOrNull()
    }

    override suspend fun add(shop: Shop.New): Int? = dbQuery {
        Shops.insert {
            it[manager] = shop.manager
            it[city] = shop.city
            it[street] = shop.street
            it[zipCode] = shop.zipCode
        }.getOrNull(Shops.id)
    }

    private fun toShop(it: ResultRow) = Shop(
        id = it[Shops.id],
        manager = it[Shops.manager],
        city = it[Shops.city],
        street = it[Shops.street],
        zipCode = it[Shops.zipCode]
    )
}

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

package videoclub.data

/**
 * A shop of the APPV chain.
 */
data class Shop(
    /** A unique id for this shop. */
    val id: Int,
    /** The name of the manager of this shop. */
    val manager: String,
    /** The city where this shop is located. */
    val city: String,
    /** The street where this shop is located. */
    val street: String,
    /** The zip code of the zone where this shop is located. */
    val zipCode: String
) {
    /**
     * Information of a new shop of the APPV chain.
     */
    data class New(
        /** The name of the manager of this shop. */
        val manager: String,
        /** The city where this shop is located. */
        val city: String,
        /** The street where this shop is located. */
        val street: String,
        /** The zip code of the zone where this shop is located. */
        val zipCode: String
    )

    /**
     * An update of the information of a shop of the APPV chain.
     */
    data class Update(
        /** The updated name of the manager of this shop. */
        val manager: String,
        /** The updated city where this shop is located. */
        val city: String,
        /** The updated street where this shop is located. */
        val street: String,
        /** The updated zip code of the zone where this shop is located. */
        val zipCode: String
    )
}

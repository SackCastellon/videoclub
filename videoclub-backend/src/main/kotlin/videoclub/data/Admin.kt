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
 * An administrator of the system.
 */
data class Admin(
    /** The user id of this admin. */
    val id: Int,
    /** The name of this admin. */
    val name: String
) {
    /**
     * Information of a new administrator of the system.
     */
    data class New(
        /** The name of this admin. */
        val name: String
    )

    /**
     * An update of the information of an administrator of the system.
     */
    data class Update(
        /** The updated name of this admin. */
        val name: String
    )
}

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

package videoclub.auth

import videoclub.data.Member

/**
 * Information required to register a new user into the system.
 */
data class RegistrationInfo(
    /**
     * The name of the new user.
     *
     * @see Member.name
     */
    val name: String,
    /**
     * The age of the new user.
     *
     * @see Member.age
     */
    val age: Int,
    /**
     * The username of the new user.
     *
     * @see Member.username
     */
    val username: String,
    /**
     * The password of the new user.
     */
    val password: String
)

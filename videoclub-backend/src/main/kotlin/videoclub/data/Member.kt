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

package videoclub.data

/**
 * A member of the APPV chain.
 */
data class Member(
    /** A unique id for this member. */
    val id: Int,
    /** The full name of the member. */
    val name: String,
    /** The age of the member. */
    val age: Int,
    /** The username of the member. */
    val username: String
)

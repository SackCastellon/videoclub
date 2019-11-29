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

import java.math.BigDecimal
import java.time.LocalDate

/**
 * A movie available to be rented at a given [Shop].
 */
data class Movie(
    /** A unique id for this movie. */
    val id: Int,
    /** The name of this movie. */
    val name: String,
    /** The director of this movie. This filed is optional. */
    val director: String?,
    /** The release date of this movie. */
    val releaseDate: LocalDate,
    /** The price to rent this movie. */
    val price: BigDecimal
)

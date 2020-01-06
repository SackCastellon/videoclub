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

package videoclub.auth

import org.koin.dsl.module
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

private const val saltLength = 24
private const val hashLength = 64
private const val parallelism = 1
private const val memory = 1 shl 14
private const val iterations = 4

internal val authModule = module {
    single<PasswordEncoder> { Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations) }
}

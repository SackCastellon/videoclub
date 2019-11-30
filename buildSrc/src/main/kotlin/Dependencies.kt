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

@file:Suppress("KDocMissingDocumentation")

import org.gradle.api.artifacts.dsl.DependencyHandler

const val kotlinVersion: String = "1.3.61"
const val logbackVersion: String = "1.2.1"
const val ktorVersion: String = "1.2.6"
const val koinVersion: String = "2.0.1"
const val exposedVersion: String = "0.18.1"

@Suppress("unused")
fun DependencyHandler.ktor(module: String, version: String = ktorVersion): Any =
    "io.ktor:ktor-$module:$version"

@Suppress("unused")
fun DependencyHandler.koin(module: String, version: String = koinVersion): Any =
    "org.koin:koin-$module:$version"

@Suppress("unused")
fun DependencyHandler.exposed(module: String, version: String = exposedVersion): Any =
    "org.jetbrains.exposed:exposed-$module:$version"

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

package videoclub

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.cio.websocket.pingPeriod
import io.ktor.http.cio.websocket.timeout
import io.ktor.http.content.EntityTagVersion
import io.ktor.http.content.OutgoingContent
import io.ktor.jackson.jackson
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.toByteArray
import io.ktor.websocket.WebSockets
import org.koin.Logger.slf4jLogger
import org.koin.ktor.ext.Koin
import videoclub.auth.AuthConfig
import videoclub.auth.User
import videoclub.auth.UserPrincipal
import videoclub.auth.authModule
import videoclub.db.DatabaseConfig
import videoclub.db.dao.daoModule
import videoclub.route.*
import java.security.MessageDigest
import java.time.Duration
import java.util.*

/**
 * Application entry point
 */
@Suppress("unused")
fun Application.module() {

    install(Authentication) {
        jwt {
            authHeader {
                val jwtToken = it.request.cookies[AuthConfig.COOKIE_JWT] ?: return@authHeader null

                HttpAuthHeader.Single("Bearer", jwtToken)
            }
            verifier(AuthConfig.verifier)
            validate { call ->
                val xsrfHeader = request.headers[AuthConfig.HEADER_XSRF]
                    ?: return@validate null
                val xsrfToken = call.payload.getClaim(AuthConfig.CLAIM_XSRF).asString()
                    ?: return@validate null

                if (xsrfHeader != xsrfToken) return@validate null

                val id = call.payload.getClaim(AuthConfig.CLAIM_ID).asInt()
                    ?: return@validate null
                val type = call.payload.getClaim(AuthConfig.CLAIM_TYPE).`as`(User.Type::class.java)
                    ?: return@validate null

                UserPrincipal(id, type)
            }
        }
    }

    install(CallLogging)

    install(ContentNegotiation) {
        jackson {
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            registerModule(JavaTimeModule())
        }
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024)
        }
    }

    install(ConditionalHeaders) {
        val encoder = Base64.getUrlEncoder().withoutPadding()
        val digest = MessageDigest.getInstance("MD5")

        fun ByteArray.version() = EntityTagVersion(encoder.encodeToString(digest.digest(this)))

        version {
            when (it) {
                is OutgoingContent.ReadChannelContent -> listOf(it.readFrom().toByteArray().version())
                is OutgoingContent.ByteArrayContent -> listOf(it.bytes().version())
                else -> emptyList()
            }
        }
    }

    install(AutoHeadResponse)

    install(CORS) {
        // Methods
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)

        // Headers
        header(HttpHeaders.ContentType)
        header(AuthConfig.HEADER_XSRF)

        exposeHeader(AuthConfig.HEADER_XSRF)

        allowCredentials = true

        anyHost() // TODO: Don't do this in production if possible. Try to limit it.
    }

    install(DefaultHeaders)

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    install(Koin) {
        slf4jLogger()

        modules(authModule)
        modules(daoModule)
    }

    routing {
        route("api") {
            auth()
            users()
            movies()
            shops()
            rentals()
            stats()
        }
    }

    DatabaseConfig.setup()
}

package videoclub

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
import videoclub.route.auth
import videoclub.route.movies
import videoclub.route.shops
import videoclub.route.users
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
        }
    }

    DatabaseConfig.setup()
}

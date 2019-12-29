package videoclub

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import org.koin.Logger.slf4jLogger
import org.koin.ktor.ext.Koin
import videoclub.auth.AuthConfig
import videoclub.auth.UserPrincipal
import videoclub.auth.authModule
import videoclub.db.DatabaseConfig
import videoclub.db.dao.daoModule
import videoclub.route.auth
import videoclub.route.member
import java.time.Duration

@Suppress("unused")
fun Application.module() {
    install(Koin) {
        slf4jLogger()

        modules(authModule)
        modules(daoModule)
    }

    DatabaseConfig.setup()

    install(Authentication) {
        jwt {
            authHeader {
                val jwtToken = it.request.cookies[AuthConfig.COOKIE_JWT] ?: return@authHeader null

                HttpAuthHeader.Single("Bearer", jwtToken)
            }
            verifier(AuthConfig.verifier)
            validate {
                val xsrfHeader = request.headers[AuthConfig.HEADER_XSRF] ?: return@validate null
                val xsrfToken = it.payload.getClaim(AuthConfig.CLAIM_XSRF).asString() ?: return@validate null
                if (xsrfHeader != xsrfToken) return@validate null

                it.payload.getClaim(AuthConfig.CLAIM_USER).asInt()?.let(::UserPrincipal)
            }
        }
    }

    install(CallLogging)

    install(ContentNegotiation) {
        jackson()
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
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

    routing {
        route("api") {
            auth()
            member()
        }
    }
}

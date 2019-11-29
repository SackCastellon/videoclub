package videoclub

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import java.time.Duration

@Suppress("unused")
fun Application.module() {
    install(Authentication) {
    }

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
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        anyHost() // TODO: Don't do this in production if possible. Try to limit it.
    }

    install(DefaultHeaders)

    install(HSTS)

    install(HttpsRedirect) {
        sslPort = environment.config.property("ktor.deployment.sslPort").getString().toInt()
    }

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
    }
}

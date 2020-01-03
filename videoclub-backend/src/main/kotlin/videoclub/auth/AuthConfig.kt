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

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.apache.commons.codec.binary.Base64
import java.util.*
import kotlin.time.minutes

internal object AuthConfig {

    private const val issuer = "Videoclub"
    private const val subject = "Authentication"
    private val secret: ByteArray = System.getenv("JWT_SECRET").let(::checkNotNull).let(Base64::decodeBase64)
    private val algorithm = Algorithm.HMAC512(secret)

    internal val tokenLifespan = 10.minutes.toLongMilliseconds()

    internal const val CLAIM_ID = "id"
    internal const val CLAIM_TYPE = "type"
    internal const val CLAIM_XSRF = "xsrf"

    internal const val COOKIE_JWT = "JWT-Token"
    internal const val COOKIE_XSRF = "XSRF-Token"
    internal const val HEADER_XSRF = COOKIE_XSRF

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withSubject(subject)
        .build()

    /**
     * Creates a _JSON Web Token_ for the given [user], with the given [xsrfToken].
     */
    fun createToken(user: User, xsrfToken: String): String = JWT
        .create()
        .withIssuer(issuer)
        .withSubject(subject)
        .withClaim(CLAIM_ID, user.id)
        .withClaim(CLAIM_TYPE, user.type.name)
        .withClaim(CLAIM_XSRF, xsrfToken)
        .withExpiresAt(expiration)
        .sign(algorithm)

    private inline val expiration: Date get() = Date(System.currentTimeMillis() + tokenLifespan)
}

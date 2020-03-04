package pl.teamkiwi.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

class ServerAuthenticationProvider internal constructor(
    configuration: Configuration
) : AuthenticationProvider(configuration) {

    internal var authServerUrlFunction = configuration.authServerUrlFunction

    internal var fetchPrincipalFunction = configuration.fetchPrincipalFunction

    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name) {

        internal var authServerUrlFunction: () -> String = {
            throw NotImplementedError(
                "auth server url not specified, use authServerUrl { ... } to fix"
            )
        }

        internal var fetchPrincipalFunction: suspend (authUrl: String, token: String) -> Principal? = {
                authUrl, token ->

            runCatching {
                val sessionUrl = "$authUrl/v1/session"

                val response = HttpClient(Apache).get<String>(sessionUrl) {
                    header(AUTHORIZATION_KEY, token)
                }

                jacksonObjectMapper().readValue<AuthPrincipal>(response)
            }.getOrNull()
        }

        fun authServerUrl(body: () -> String) {
            authServerUrlFunction = body
        }

        fun fetchPrincipal(body: suspend (String, String) -> Principal?) {
            fetchPrincipalFunction = body
        }
    }
}

/**
 * Installs Server Authentication mechanism
 */
fun Authentication.Configuration.server(
    name: String? = null,
    configure: ServerAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = ServerAuthenticationProvider(ServerAuthenticationProvider.Configuration(name).apply(configure))
    val authServerUrl = provider.authServerUrlFunction
    val fetchPrincipal = provider.fetchPrincipalFunction

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->

        val authUrl = authServerUrl()
        val token = call.request.headers[AUTHORIZATION_KEY]
        val principal = token?.let { fetchPrincipal(authUrl, it) }

        val cause = when {
            token == null -> AuthenticationFailedCause.NoCredentials
            principal == null -> AuthenticationFailedCause.InvalidCredentials
            else -> null
        }

        if (cause != null) {
            context.challenge(serverAuthenticationChallengeKey, cause) {
                call.respond(HttpStatusCode.Unauthorized, "")
                it.complete()
            }
        }
        if (principal != null) {
            context.principal(principal)
        }
    }

    register(provider)
}

private val serverAuthenticationChallengeKey: Any = "ServerAuth"

const val AUTHORIZATION_KEY = "Authorization"

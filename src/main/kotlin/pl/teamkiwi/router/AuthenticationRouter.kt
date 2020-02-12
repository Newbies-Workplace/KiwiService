package pl.teamkiwi.router

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.koin.ktor.ext.inject
import pl.teamkiwi.controller.AuthenticationController
import pl.teamkiwi.controller.AuthenticationController.Companion.AUTH_SESSION_KEY
import pl.teamkiwi.exception.UnauthorizedException
import pl.teamkiwi.model.request.UserLoginRequest
import pl.teamkiwi.util.safeReceiveOrNull

fun Routing.authenticationRoutes() {
    val authenticationController: AuthenticationController by inject()

    post("v1/login") {
        val loginRequest = call.safeReceiveOrNull<UserLoginRequest>() ?: throw UnauthorizedException()

        val session = authenticationController.login(loginRequest)

        call.sessions.set(session)
        call.respond("")
    }

    authenticate {
        get("v1/logout") {
            call.sessions.clear(AUTH_SESSION_KEY)
            call.respond("")
        }
    }
}
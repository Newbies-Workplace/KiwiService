package pl.teamkiwi

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import pl.teamkiwi.auth.AuthPrincipal
import pl.teamkiwi.exception.UnauthorizedException

fun Application.testModule() {
    routing {

        /**
         * Test function, that responds with [AuthPrincipal]
         *
         * For test purposes, token's owner id (User id) is equal to auth token
         */
        get("/v1/session") {
            val token = call.request.headers["Authorization"] ?: throw UnauthorizedException()

            val principal = AuthPrincipal(token)

            call.respond(principal)
        }
    }
}
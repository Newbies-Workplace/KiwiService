package pl.teamkiwi.router

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject
import pl.teamkiwi.controller.UserController
import pl.teamkiwi.exception.BadRequestException
import pl.teamkiwi.exception.UnauthorizedException
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.util.authPrincipal
import pl.teamkiwi.util.idParameter

fun Routing.userRoutes() {
    val userController: UserController by inject()

    authenticate {
        post("/v1/user") {
            val principal = call.authPrincipal() ?: throw UnauthorizedException()
            val userCreateRequest = call.receiveOrNull<UserCreateRequest>() ?: throw BadRequestException()

            val response = userController.createUser(principal.userId, userCreateRequest)

            call.respond(HttpStatusCode.Created, response)
        }

        get("/v1/users") {
            call.respond(userController.getAllUsers())
        }

        get("/v1/user/{id}") {
            val id = call.idParameter()

            val response = userController.getUserById(id)

            call.respond(response)
        }
    }

    authenticate {
        get("/v1/test") {
            val principal = call.authPrincipal() ?: throw UnauthorizedException()

            call.respond(principal)
        }
    }
}
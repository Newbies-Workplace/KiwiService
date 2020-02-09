package pl.teamkiwi.router

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject
import pl.teamkiwi.controller.UserController
import pl.teamkiwi.exception.BadRequestException
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.util.idParameter
import pl.teamkiwi.util.safeReceiveOrNull

fun Routing.userRoutes() {
    val userController: UserController by inject()

    get("/") {
        call.respond("Nothing here :o")
    }

    post("/user") {
        val userCreateRequest = call.safeReceiveOrNull<UserCreateRequest>() ?: throw BadRequestException()

        val response = userController.createUser(userCreateRequest)

        call.respond(HttpStatusCode.Created, response)
    }

    get("/users") {
        call.respond(userController.getAllUsers())
    }

    get("/user/{id}") {
        val id = call.idParameter()

        val response = userController.getUserById(id)

        call.respond(response)
    }
}
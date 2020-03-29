package pl.teamkiwi.application.controller

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import pl.teamkiwi.application.model.request.UserCreateRequest
import pl.teamkiwi.application.util.authPrincipal
import pl.teamkiwi.application.util.myReceive
import pl.teamkiwi.domain.model.exception.BadRequestException
import pl.teamkiwi.domain.service.UserService

class UserController(
    private val userService: UserService
) {

    suspend fun postUser(call: ApplicationCall) {
        val principal = call.authPrincipal()
        val userCreateRequest = call.myReceive<UserCreateRequest>(BadRequestException())

        val response = userService.createUser(principal.userId, userCreateRequest)

        call.respond(HttpStatusCode.Created, response)
    }

    suspend fun getAllUsers(call: ApplicationCall) {
        call.respond(userService.getAllUsers())
    }

    suspend fun getUserById(call: ApplicationCall, id: String) {
        val response = userService.getUserById(id)

        call.respond(response)
    }
}
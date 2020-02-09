package pl.teamkiwi.controller

import pl.teamkiwi.converter.UserConverter
import pl.teamkiwi.converter.toUserResponse
import pl.teamkiwi.exception.EmailOccupiedException
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.service.UserService
import java.util.*

class UserController (
    private val userService: UserService,
    private val userConverter: UserConverter
) {

    fun createUser(userCreateRequest: UserCreateRequest): UserResponse {
        if (userService.findByEmail(userCreateRequest.email) != null) {
            throw EmailOccupiedException()
        }

        val userCreateDTO = with(userConverter) { userCreateRequest.toUserCreateDTO() }

        return userService.save(userCreateDTO).toUserResponse()
    }

    fun getUserById(id: UUID): UserResponse {
        return userService.findById(id)?.toUserResponse() ?: throw NotFoundException()
    }

    fun getAllUsers(): List<UserResponse> {
        val users = userService.getAll()

        if (users.isEmpty()) {
            throw NoContentException()
        }

        return users.map { it.toUserResponse() }
    }
}

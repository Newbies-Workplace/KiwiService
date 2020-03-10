package pl.teamkiwi.controller

import pl.teamkiwi.converter.toUserCreateDTO
import pl.teamkiwi.converter.toUserResponse
import pl.teamkiwi.exception.AccountAlreadyExistsException
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.service.UserService

class UserController (
    private val userService: UserService
) {

    fun createUser(userId: String, userCreateRequest: UserCreateRequest): UserResponse {
        if (userService.findById(userId) != null) throw AccountAlreadyExistsException()

        val userCreateDTO = userCreateRequest.toUserCreateDTO(userId)

        return userService.save(userId, userCreateDTO).toUserResponse()
    }

    fun getUserById(id: String): UserResponse {
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

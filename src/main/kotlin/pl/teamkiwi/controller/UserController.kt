package pl.teamkiwi.controller

import pl.teamkiwi.exception.EmailOccupiedException
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.service.UserService
import java.util.*

class UserController (
    private val userService: UserService
) {

    fun createUser(userCreateRequest: UserCreateRequest): UserDTO {
        if (userService.findByEmail(userCreateRequest.email) != null) {
            throw EmailOccupiedException()
        }

        return userService.save(userCreateRequest)
    }

    fun getUserById(id: UUID): UserDTO {
        return userService.findById(id) ?: throw NotFoundException()
    }

    fun getAllUsers(): List<UserDTO> {
        val users = userService.getAll()

        if (users.isEmpty()) {
            throw NoContentException()
        }

        return users
    }
}
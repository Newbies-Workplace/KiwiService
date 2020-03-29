package pl.teamkiwi.domain.service

import org.joda.time.DateTime
import pl.teamkiwi.application.converter.toUserResponse
import pl.teamkiwi.application.model.request.UserCreateRequest
import pl.teamkiwi.application.model.response.UserResponse
import pl.teamkiwi.domain.`interface`.UserRepository
import pl.teamkiwi.domain.model.entity.User
import pl.teamkiwi.domain.model.exception.AccountAlreadyExistsException
import pl.teamkiwi.domain.model.exception.NoContentException
import pl.teamkiwi.domain.model.exception.NotFoundException

class UserService (
    private val userRepository: UserRepository
) {

    fun createUser(userId: String, request: UserCreateRequest): UserResponse {
        if (userRepository.findById(userId) != null) throw AccountAlreadyExistsException()

        val user =
            User(
                id = userId,
                username = request.username,
                description = request.description,
                creationDate = DateTime.now()
            )

        return userRepository.save(user).toUserResponse()
    }

    fun getUserById(id: String): UserResponse {
        return userRepository.findById(id)?.toUserResponse() ?: throw NotFoundException()
    }

    fun getAllUsers(): List<UserResponse> {
        val users = userRepository.findAll()

        if (users.isEmpty()) {
            throw NoContentException()
        }

        return users.map { it.toUserResponse() }
    }
}

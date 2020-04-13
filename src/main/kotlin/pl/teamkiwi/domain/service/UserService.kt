package pl.teamkiwi.domain.service

import org.joda.time.DateTime
import pl.teamkiwi.application.model.request.UserCreateRequest
import pl.teamkiwi.domain.`interface`.UserRepository
import pl.teamkiwi.domain.model.entity.User
import pl.teamkiwi.domain.model.exception.AccountAlreadyExistsException

class UserService (
    private val userRepository: UserRepository
) {

    fun createUser(userId: String, request: UserCreateRequest): User {
        if (userRepository.findById(userId) != null) throw AccountAlreadyExistsException("Account with specified id already exists.")

        val user =
            User(
                id = userId,
                username = request.username,
                description = request.description,
                creationDate = DateTime.now()
            )

        return userRepository.save(user)
    }

    fun getUserById(id: String): User? =
        userRepository.findById(id)

    fun getAllUsers(): List<User> =
        userRepository.findAll()
}

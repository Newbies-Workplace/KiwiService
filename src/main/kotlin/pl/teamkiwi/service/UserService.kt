package pl.teamkiwi.service

import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.repository.UserRepository
import java.util.*

class UserService (
    private val userRepository: UserRepository
) {

    fun save(userCreateRequest: UserCreateRequest): UserDTO =
        userRepository.save(userCreateRequest)

    fun findById(id: UUID): UserDTO? =
        userRepository.findById(id)

    fun findByEmail(email: String): UserDTO? =
        userRepository.findByEmail(email)

    fun getAll(): List<UserDTO> =
        userRepository.getAll()
}
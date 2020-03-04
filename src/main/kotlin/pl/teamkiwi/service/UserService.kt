package pl.teamkiwi.service

import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.dto.create.UserCreateDTO
import pl.teamkiwi.repository.UserRepository
import java.util.*

class UserService (
    private val userRepository: UserRepository
) {

    fun save(id: UUID, userCreateDTO: UserCreateDTO): UserDTO =
        userRepository.save(id, userCreateDTO)

    fun findById(id: UUID): UserDTO? =
        userRepository.findById(id)

    fun getAll(): List<UserDTO> =
        userRepository.getAll()
}
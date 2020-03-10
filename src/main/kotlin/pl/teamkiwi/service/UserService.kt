package pl.teamkiwi.service

import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.dto.create.UserCreateDTO
import pl.teamkiwi.repository.UserRepository

class UserService (
    private val userRepository: UserRepository
) {

    fun save(id: String, userCreateDTO: UserCreateDTO): UserDTO =
        userRepository.save(id, userCreateDTO)

    fun findById(id: String): UserDTO? =
        userRepository.findById(id)

    fun getAll(): List<UserDTO> =
        userRepository.getAll()
}
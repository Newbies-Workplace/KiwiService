package pl.teamkiwi.repository

import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.converter.toUserDTO
import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.dto.create.UserCreateDTO
import pl.teamkiwi.repository.dao.UserDAO

class UserRepository {

    fun save(id: String, user: UserCreateDTO): UserDTO =
        transaction {
            UserDAO.new(id) {
                username = user.username
                description = user.description
                avatarPath = user.avatarPath
                creationDate = user.creationDate
            }.toUserDTO()
        }

    fun findById(id: String): UserDTO? =
        transaction {
            UserDAO.findById(id)
                ?.toUserDTO()
        }

    fun getAll(): List<UserDTO> =
        transaction {
            UserDAO.all()
                .map { it.toUserDTO() }
        }
}
package pl.teamkiwi.repository

import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.converter.toUserDTO
import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.dto.create.UserCreateDTO
import pl.teamkiwi.repository.dao.UserDAO
import pl.teamkiwi.repository.table.Users
import java.util.*

class UserRepository {

    fun save(user: UserCreateDTO): UserDTO =
        transaction {
            UserDAO.new {
                email = user.email
                username = user.username
                description = user.description
                avatarPath = user.avatarPath
                passwordHash = user.passwordHash
                creationDate = user.creationDate
            }.toUserDTO()
        }

    fun findById(id: UUID): UserDTO? =
        transaction {
            UserDAO.findById(id)
                ?.toUserDTO()
        }

    fun findByEmail(email: String): UserDTO? =
        transaction {
            UserDAO.find { Users.email eq email }
                .firstOrNull()
                ?.toUserDTO()
        }

    fun getAll(): List<UserDTO> =
        transaction {
            UserDAO.all()
                .map { it.toUserDTO() }
        }
}
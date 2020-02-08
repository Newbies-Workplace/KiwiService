package pl.teamkiwi.repository

import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.converter.toUserDTO
import pl.teamkiwi.converter.toUserResponse
import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.repository.dao.UserDAO
import pl.teamkiwi.repository.table.Users
import java.util.*

class UserRepository {

    fun save(user: UserCreateRequest): UserDTO =
        transaction {
            UserDAO.new {
                email = user.email
                username = user.username
                description = user.description
                //todo hash password
                passwordHash = user.password
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
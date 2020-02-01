package pl.teamkiwi.repository

import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.converter.toUser
import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.request.UserCreateRequest
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
            }.toUser()
        }

    fun findById(id: UUID): UserDTO? =
        transaction {
            UserDAO.findById(id)
                ?.toUser()
        }

    fun findByEmail(email: String): UserDTO? =
        transaction {
            UserDAO.find { Users.email eq email }
                .firstOrNull()
                ?.toUser()
        }

    fun getAll(): List<UserDTO> =
        transaction {
            UserDAO.all()
                .map { it.toUser() }
        }
}
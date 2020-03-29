package pl.teamkiwi.infrastructure.repository

import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.domain.`interface`.UserRepository
import pl.teamkiwi.domain.model.entity.User
import pl.teamkiwi.infrastructure.repository.dao.UserDAO

class UserExposedRepository : UserRepository {

    override fun save(user: User): User =
        transaction {
            UserDAO.new(user.id) {
                username = user.username
                description = user.description
                avatarPath = user.avatarPath
                creationDate = user.creationDate
            }.toUser()
        }

    override fun findById(id: String): User? =
        transaction {
            UserDAO.findById(id)
                ?.toUser()
        }

    override fun findAll(): List<User> =
        transaction {
            UserDAO.all()
                .map { it.toUser() }
        }
}
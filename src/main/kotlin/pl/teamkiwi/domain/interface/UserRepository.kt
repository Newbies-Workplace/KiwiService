package pl.teamkiwi.domain.`interface`

import pl.teamkiwi.domain.model.entity.User

interface UserRepository {

    fun save(user: User): User

    fun findById(id: String): User?

    fun findAll(): List<User>
}
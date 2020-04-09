package pl.teamkiwi.infrastructure.repository.exposed.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.domain.model.entity.User
import pl.teamkiwi.infrastructure.repository.exposed.table.StringIdEntity
import pl.teamkiwi.infrastructure.repository.exposed.table.StringIdEntityClass
import pl.teamkiwi.infrastructure.repository.exposed.table.Users

class UserDAO(id: EntityID<String>) : StringIdEntity(id) {
    companion object : StringIdEntityClass<UserDAO>(Users)

    var username by Users.username
    var description by Users.description
    var creationDate by Users.creationDate

    fun toUser() =
        User(
            id = id.value,
            username = username,
            description = description,
            creationDate = creationDate
        )
}

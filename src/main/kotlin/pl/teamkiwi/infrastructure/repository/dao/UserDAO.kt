package pl.teamkiwi.infrastructure.repository.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.domain.model.entity.User
import pl.teamkiwi.infrastructure.repository.table.StringIdEntity
import pl.teamkiwi.infrastructure.repository.table.StringIdEntityClass
import pl.teamkiwi.infrastructure.repository.table.Users

class UserDAO(id: EntityID<String>) : StringIdEntity(id) {
    companion object : StringIdEntityClass<UserDAO>(Users)

    var username by Users.username
    var description by Users.description
    var avatarPath by Users.avatarPath
    var creationDate by Users.creationDate

    fun toUser() =
        User(
            id = id.value,
            username = username,
            description = description,
            avatarPath = avatarPath,
            creationDate = creationDate
        )
}

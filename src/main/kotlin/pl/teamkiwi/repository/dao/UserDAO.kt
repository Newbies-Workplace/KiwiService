package pl.teamkiwi.repository.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.repository.table.StringIdEntity
import pl.teamkiwi.repository.table.StringIdEntityClass
import pl.teamkiwi.repository.table.Users

class UserDAO(id: EntityID<String>) : StringIdEntity(id) {
    companion object : StringIdEntityClass<UserDAO>(Users)

    var username by Users.username
    var description by Users.description
    var avatarPath by Users.avatarPath
    var creationDate by Users.creationDate
}

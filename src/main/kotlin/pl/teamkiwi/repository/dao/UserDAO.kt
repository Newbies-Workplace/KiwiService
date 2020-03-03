package pl.teamkiwi.repository.dao

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import pl.teamkiwi.repository.table.Users
import java.util.*

class UserDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDAO>(Users)

    var username by Users.username
    var description by Users.description
    var avatarPath by Users.avatarPath
    var creationDate by Users.creationDate
}

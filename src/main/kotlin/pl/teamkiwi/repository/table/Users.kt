package pl.teamkiwi.repository.table

import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import pl.teamkiwi.repository.constants.DESCRIPTION_MAX_LENGTH
import pl.teamkiwi.repository.constants.EMAIL_MAX_LENGTH
import pl.teamkiwi.repository.constants.PASSWORD_HASH_MAX_LENGTH
import pl.teamkiwi.repository.constants.USERNAME_MAX_LENGTH

/**
 * Database table
 */
object Users : UUIDTable() {
    val email = varchar("email", EMAIL_MAX_LENGTH).uniqueIndex()
    val username = varchar("username", USERNAME_MAX_LENGTH)
    val description = varchar("description", DESCRIPTION_MAX_LENGTH).nullable()
    val avatarPath = varchar("avatarPath", 150).nullable()
    val passwordHash = varchar("passwordHash", PASSWORD_HASH_MAX_LENGTH)
    val creationDate = datetime("creationDate").default(DateTime.now())
}
package pl.teamkiwi.repository.table

import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import pl.teamkiwi.repository.constants.DESCRIPTION_MAX_LENGTH
import pl.teamkiwi.repository.constants.EMAIL_MAX_LENGTH
import pl.teamkiwi.repository.constants.USERNAME_MAX_LENGTH

/**
 * Database table
 */
object Users : UUIDTable() {
    val email = varchar("email", EMAIL_MAX_LENGTH).uniqueIndex()
    val username = varchar("username", USERNAME_MAX_LENGTH)
    val description = varchar("description", DESCRIPTION_MAX_LENGTH).nullable()
    val avatarPath = varchar("avatarPath", 150).nullable()
    val passwordHash = varchar("passwordHash", 100)
    //todo check if default value is correct
    val creationDate = date("creationDate").default(DateTime())
}
package pl.teamkiwi.repository.table

import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime

/**
 * Database table
 */
object Users : UUIDTable() {
    val username = varchar("username", USERNAME_MAX_LENGTH)
    val description = varchar("description", DESCRIPTION_MAX_LENGTH).nullable()
    val avatarPath = varchar("avatarPath", 150).nullable()
    val creationDate = datetime("creationDate").default(DateTime.now())
}

const val USERNAME_MAX_LENGTH = 50
const val DESCRIPTION_MAX_LENGTH = 200
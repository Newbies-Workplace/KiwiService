package pl.teamkiwi.repository.table

import org.joda.time.DateTime
import pl.teamkiwi.repository.table.Constants.DEFAULT_CHARSET

/**
 * Database table
 */
object Users : StringIdTable() {
    val username = varchar("username", USERNAME_MAX_LENGTH, DEFAULT_CHARSET)
    val description = varchar("description", DESCRIPTION_MAX_LENGTH, DEFAULT_CHARSET).nullable()
    val avatarPath = varchar("avatarPath", 150, DEFAULT_CHARSET).nullable()
    val creationDate = datetime("creationDate").default(DateTime.now())
}

const val USERNAME_MAX_LENGTH = 50
const val DESCRIPTION_MAX_LENGTH = 200
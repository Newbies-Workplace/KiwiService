package pl.teamkiwi.repository.table

import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime

/**
 * Database table
 */
object Users : UUIDTable() {
    val email = varchar("email", 50).uniqueIndex()
    val username = varchar("username", 50)
    val description = varchar("description", 200).nullable()
    val avatarPath = varchar("avatarPath", 150).nullable()
    val passwordHash = varchar("passwordHash", 100)
    //todo check if default value is correct
    val creationDate = date("creationDate").default(DateTime())
}
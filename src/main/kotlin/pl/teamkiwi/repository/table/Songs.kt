package pl.teamkiwi.repository.table

import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import pl.teamkiwi.repository.constants.IMAGE_PATH_MAX_LENGTH
import pl.teamkiwi.repository.constants.PATH_MAX_LENGTH
import pl.teamkiwi.repository.constants.TITLE_MAX_LENGTH

/**
 * Database table
 */
object Songs : UUIDTable() {
    val title = varchar("title", TITLE_MAX_LENGTH)
    val path = varchar("path", PATH_MAX_LENGTH)
    val imagePath = varchar("imagePath", IMAGE_PATH_MAX_LENGTH).nullable()
    val artistId = uuid("artistId")
    val duration = long("duration")
    val uploadDate = datetime("uploadDate").default(DateTime.now())
}
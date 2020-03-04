package pl.teamkiwi.repository.table

import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import pl.teamkiwi.repository.constants.ARTIST_MAX_LENGTH
import pl.teamkiwi.repository.constants.IMAGE_PATH_MAX_LENGTH
import pl.teamkiwi.repository.constants.TITLE_MAX_LENGTH

/**
 * Database table
 */
object Albums : UUIDTable() {
    val title = varchar("title", TITLE_MAX_LENGTH)
    val imagePath = varchar("imagePath", IMAGE_PATH_MAX_LENGTH).nullable()
    val artist = varchar("artist", ARTIST_MAX_LENGTH).nullable()
    val artistId = entityId("artistId", Users)
    val composeDate = date("composeDate")
    val uploadDate = date("uploadDate").default(DateTime())
}
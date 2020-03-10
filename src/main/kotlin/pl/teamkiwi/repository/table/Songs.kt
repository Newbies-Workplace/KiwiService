package pl.teamkiwi.repository.table

import org.joda.time.DateTime
import pl.teamkiwi.repository.constants.IMAGE_PATH_MAX_LENGTH
import pl.teamkiwi.repository.constants.PATH_MAX_LENGTH
import pl.teamkiwi.repository.constants.TITLE_MAX_LENGTH
import pl.teamkiwi.repository.table.Constants.DEFAULT_CHARSET

/**
 * Database table
 */
object Songs : StringIdTable() {
    val title = varchar("title", TITLE_MAX_LENGTH, DEFAULT_CHARSET)
    val path = varchar("path", PATH_MAX_LENGTH, DEFAULT_CHARSET)
    val imagePath = varchar("imagePath", IMAGE_PATH_MAX_LENGTH, DEFAULT_CHARSET).nullable()
    val artistId = uuid("artistId")
    val duration = long("duration")
    val uploadDate = datetime("uploadDate").default(DateTime.now())
}
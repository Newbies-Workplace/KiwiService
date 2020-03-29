package pl.teamkiwi.infrastructure.repository.table

import org.joda.time.DateTime
import pl.teamkiwi.infrastructure.repository.constants.IMAGE_PATH_MAX_LENGTH
import pl.teamkiwi.infrastructure.repository.constants.TITLE_MAX_LENGTH

object Albums : StringIdTable() {
    val title = varchar("title", TITLE_MAX_LENGTH, Constants.DEFAULT_CHARSET)
    val imagePath = varchar("imagePath", IMAGE_PATH_MAX_LENGTH, Constants.DEFAULT_CHARSET).nullable()
    val artistId = varchar("artistId", STRING_UUID_LENGTH, Constants.DEFAULT_CHARSET)
    val uploadDate = datetime("uploadDate").default(DateTime.now())
}
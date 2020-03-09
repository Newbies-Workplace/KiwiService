package pl.teamkiwi.model.dto.create

import org.joda.time.DateTime
import java.util.*

data class SongCreateDTO(
    val title: String,
    val path: String,
    val imagePath: String?,
    val artistId: UUID,
    val duration: Long,
    val uploadDate: DateTime
)
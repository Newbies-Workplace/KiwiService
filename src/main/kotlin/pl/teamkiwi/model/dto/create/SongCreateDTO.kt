package pl.teamkiwi.model.dto.create

import org.joda.time.DateTime

data class SongCreateDTO(
    val title: String,
    val path: String,
    val imagePath: String?,
    val artistId: String,
    val duration: Long,
    val uploadDate: DateTime
)
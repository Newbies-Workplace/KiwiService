package pl.teamkiwi.model.dto.create

import org.joda.time.DateTime

data class AlbumCreateDTO(
    val title: String,
    val artistId: String,
    val imagePath: String?,
    val uploadDate: DateTime
)
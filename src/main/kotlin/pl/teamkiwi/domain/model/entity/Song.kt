package pl.teamkiwi.domain.model.entity

import org.joda.time.DateTime

data class Song(
    val id: String,
    val title: String,
    val imageFile: ImageFile?,
    val artistId: String,
    val albumId: String?,
    val file: SongFile,
    val duration: Long,
    val uploadDate: DateTime
)
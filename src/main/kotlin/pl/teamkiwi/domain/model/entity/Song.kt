package pl.teamkiwi.domain.model.entity

import org.joda.time.DateTime

data class Song(
    override val id: String,
    override val title: String,
    override val imagePath: String?,
    override val artistId: String,
    val albumId: String?,
    val path: String,
    val duration: Long,
    val uploadDate: DateTime
) : MediaItem(
    id = id,
    title = title,
    artistId = artistId,
    imagePath = imagePath
)
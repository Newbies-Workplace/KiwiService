package pl.teamkiwi.domain.model.entity

import org.joda.time.DateTime

data class Album(
    override val id: String,
    override val title: String,
    override val artistId: String,
    override val imagePath: String?,
    val uploadDate: DateTime
) : MediaItem(
    id = id,
    title = title,
    artistId = artistId,
    imagePath = imagePath
)
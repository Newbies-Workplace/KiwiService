package pl.teamkiwi.model.dto

import java.util.*

data class AlbumDTO(
    override val id: String,
    override val title: String,
    override val artistId: String,
    override val imagePath: String?,
    val uploadDate: Date
) : MediaItemDTO(
    id = id,
    title = title,
    artistId = artistId,
    imagePath = imagePath
)
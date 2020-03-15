package pl.teamkiwi.model.dto

import java.util.*

data class SongDTO(
    override val id: String,
    override val title: String,
    override val imagePath: String?,
    override val artistId: String,
    val albumId: String?,
    val path: String,
    val duration: Long,
    val uploadDate: Date
) : MediaItemDTO(
    id = id,
    title = title,
    artistId = artistId,
    imagePath = imagePath
)
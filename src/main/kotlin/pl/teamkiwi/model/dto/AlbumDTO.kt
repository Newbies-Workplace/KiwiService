package pl.teamkiwi.model.dto

import java.util.*

data class AlbumDTO(
    override val id: String,
    override val title: String,
    override val artist: String?,
    override val artistId: String,
    override val imagePath: String?,
    override val composeDate: Date?
) : MediaItemDTO(id, title, artist, artistId, imagePath, composeDate)
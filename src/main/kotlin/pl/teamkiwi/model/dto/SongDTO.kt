package pl.teamkiwi.model.dto

import java.util.*

data class SongDTO(
    override val id: String,
    override val title: String,
    override val imagePath: String?,
    override val artist: String?,
    override val artistId: String,
    override val composeDate: Date?,
    val uploadDate: Date,
    val path: String,
    val albumId: String?,
    val duration: Long
): MediaItemDTO(id, title, imagePath, artistId, artist, composeDate)
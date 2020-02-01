package pl.teamkiwi.model.dto

import java.util.*

data class SongDTO(
    val id: String,
    val title: String,
    val author: UserDTO,
    val url: String,
    val composeDate: Date?,
    val uploadDate: Date,
    val album: AlbumDTO?,
    val duration: Long
)
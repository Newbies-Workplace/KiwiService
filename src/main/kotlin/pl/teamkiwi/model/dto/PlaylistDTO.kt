package pl.teamkiwi.model.dto

import java.util.*

data class PlaylistDTO(
    val id: String,
    val title: String,
    val description: String?,
    val imagePath: String?,
    val creationDate: Date,
    val songs: List<SongDTO>,
    val participants: List<PlaylistParticipantDTO>
)
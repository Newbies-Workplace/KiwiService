package pl.teamkiwi.model.dto

data class PlaylistParticipantDTO(
    val user: UserDTO,
    val playlist: PlaylistDTO,
    val canEdit: Boolean
)
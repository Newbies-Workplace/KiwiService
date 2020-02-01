package pl.teamkiwi.model.dto

data class LastPlaybackDTO(
    val user: UserDTO,
    val history: List<SongDTO>,
    val nextQueue: List<SongDTO>,
    val lastSongId: String?,
    val lastSongStopTime: Long?
)
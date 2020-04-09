package pl.teamkiwi.application.model.response

data class SongResponse(
    val id: String,
    val title: String,
    val imagePath: String?,
    val artistId: String,
    val albumId: String?,
    val path: String,
    val duration: Long,
    val uploadDate: Long
)
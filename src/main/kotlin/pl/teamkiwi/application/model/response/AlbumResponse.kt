package pl.teamkiwi.application.model.response

data class AlbumResponse(
    val id: String,
    val title: String,
    val artistId: String,
    val imagePath: String?,
    val uploadDate: Long,
    val songs: List<String>
)
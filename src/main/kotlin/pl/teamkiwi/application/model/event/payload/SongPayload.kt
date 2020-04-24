package pl.teamkiwi.application.model.event.payload

data class SongPayload(
    val id: String,
    val title: String,
    val path: String,
    val imagePath: String?,
    val artistId: String
)
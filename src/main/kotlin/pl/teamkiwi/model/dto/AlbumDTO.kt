package pl.teamkiwi.model.dto

data class AlbumDTO(
    override val id: String,
    override val title: String,
    override val artistId: String,
    override val imagePath: String?
) : MediaItemDTO(
    id = id,
    title = title,
    artistId = artistId,
    imagePath = imagePath
)
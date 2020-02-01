package pl.teamkiwi.model.dto

data class AlbumDTO(
    val id: String,
    val title: String,
    val authorString: String,
    val authorUser: UserDTO?,
    val imagePath: String?,
    val composeDate: String?
)
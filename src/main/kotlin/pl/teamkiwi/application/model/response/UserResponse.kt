package pl.teamkiwi.application.model.response

data class UserResponse(
    val id: String,
    val username: String,
    val description: String?,
    val creationDate: Long
)
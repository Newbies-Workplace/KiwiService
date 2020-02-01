package pl.teamkiwi.model.request

class UserCreateRequest(
    val email: String,
    val username: String,
    val description: String?,
    val password: String
)
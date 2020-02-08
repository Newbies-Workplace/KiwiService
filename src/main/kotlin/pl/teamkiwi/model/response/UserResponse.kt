package pl.teamkiwi.model.response

import java.util.*

data class UserResponse(
    val id: String,
    val email: String,
    val username: String,
    val description: String?,
    val avatarPath: String?,
    val creationDate: Date
)
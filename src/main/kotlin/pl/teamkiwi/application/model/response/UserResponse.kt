package pl.teamkiwi.application.model.response

import java.util.*

data class UserResponse(
    val id: String,
    val username: String,
    val description: String?,
    val creationDate: Date
)
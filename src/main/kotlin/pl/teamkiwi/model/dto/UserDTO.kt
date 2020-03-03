package pl.teamkiwi.model.dto

import java.util.*

data class UserDTO(
    val id: String,
    val username: String,
    val description: String?,
    val avatarPath: String?,
    val creationDate: Date
)
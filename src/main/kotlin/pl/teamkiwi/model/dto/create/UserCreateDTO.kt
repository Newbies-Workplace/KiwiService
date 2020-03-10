package pl.teamkiwi.model.dto.create

import org.joda.time.DateTime

data class UserCreateDTO(
    val id: String,
    val username: String,
    val description: String?,
    val avatarPath: String?,
    val creationDate: DateTime
)
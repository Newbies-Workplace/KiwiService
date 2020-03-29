package pl.teamkiwi.domain.model.entity

import org.joda.time.DateTime

data class User(
    val id: String,
    val username: String,
    val description: String? = null,
    val avatarPath: String? = null,
    val creationDate: DateTime
)
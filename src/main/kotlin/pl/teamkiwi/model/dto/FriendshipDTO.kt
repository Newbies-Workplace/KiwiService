package pl.teamkiwi.model.dto

import java.util.*

data class FriendshipDTO(
    val id: String,
    val firstUser: UserDTO,
    val secondUser: UserDTO,
    val friendsSince: Date
)
package pl.teamkiwi.model.dto

import java.util.*

data class LikeDTO(
    val user: UserDTO,
    val likedId: String,
    val likedDate: Date
)
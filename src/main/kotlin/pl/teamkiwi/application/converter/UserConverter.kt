package pl.teamkiwi.application.converter

import pl.teamkiwi.application.model.response.UserResponse
import pl.teamkiwi.domain.model.entity.User

fun User.toUserResponse() =
    UserResponse(
        id = id,
        username = username,
        description = description,
        avatarPath = avatarPath,
        creationDate = creationDate.toDate()
    )

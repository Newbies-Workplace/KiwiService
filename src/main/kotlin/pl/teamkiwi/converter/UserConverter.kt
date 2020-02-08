package pl.teamkiwi.converter

import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.repository.dao.UserDAO

fun UserDAO.toUserDTO() =
    UserDTO(
        id = id.toString(),
        email = email,
        username = username,
        description = description,
        avatarPath = avatarPath,
        passwordHash = passwordHash,
        creationDate = creationDate.toDate()
    )
fun UserDTO.toUserResponse() =
    UserResponse(
        id = id,
        email = email,
        username = username,
        description = description,
        avatarPath = avatarPath,
        creationDate = creationDate
)
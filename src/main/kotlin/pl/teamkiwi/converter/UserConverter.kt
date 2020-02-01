package pl.teamkiwi.converter

import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.repository.dao.UserDAO

fun UserDAO.toUser() =
    UserDTO(
        id = id.toString(),
        email = email,
        username = username,
        description = description,
        avatarPath = avatarPath,
        passwordHash = passwordHash,
        creationDate = creationDate.toDate()
    )
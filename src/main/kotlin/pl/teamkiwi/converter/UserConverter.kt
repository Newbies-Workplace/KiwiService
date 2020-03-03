package pl.teamkiwi.converter

import org.joda.time.DateTime
import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.dto.create.UserCreateDTO
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.repository.dao.UserDAO
import java.util.*

fun UserDAO.toUserDTO() =
    UserDTO(
        id = id.toString(),
        username = username,
        description = description,
        avatarPath = avatarPath,
        creationDate = creationDate.toDate()
    )

fun UserDTO.toUserResponse() =
    UserResponse(
        id = id,
        username = username,
        description = description,
        avatarPath = avatarPath,
        creationDate = creationDate
    )

fun UserCreateRequest.toUserCreateDTO(id: UUID) =
    UserCreateDTO(
        id = id,
        username = username,
        description = description,
        avatarPath = null,
        creationDate = DateTime.now()
    )

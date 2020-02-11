package pl.teamkiwi.converter

import org.joda.time.DateTime
import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.dto.create.UserCreateDTO
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.repository.dao.UserDAO
import pl.teamkiwi.security.PasswordEncoder
import pl.teamkiwi.session.UserPrincipal

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

fun UserDTO.toUserPrincipal() =
    UserPrincipal(
        id = id,
        email = email,
        username = username
    )

class UserConverter(
    private val passwordEncoder: PasswordEncoder
) {

    fun UserCreateRequest.toUserCreateDTO() =
        UserCreateDTO(
            email = email,
            username = username,
            description = description,
            avatarPath = null,
            passwordHash = passwordEncoder.encode(password),
            creationDate = DateTime.now()
        )
}
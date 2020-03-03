package pl.teamkiwi.model.dto.create

import org.joda.time.DateTime
import java.util.*

data class UserCreateDTO(
    val id: UUID,
    val username: String,
    val description: String?,
    val avatarPath: String?,
    val creationDate: DateTime
)
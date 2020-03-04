package pl.teamkiwi.model.request

import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import pl.teamkiwi.repository.table.DESCRIPTION_MAX_LENGTH
import pl.teamkiwi.repository.table.USERNAME_MAX_LENGTH

data class UserCreateRequest(
    val username: String,
    val description: String?
) {

    init {
        validate(this) {
            validate(UserCreateRequest::username)
                .isNotBlank()
                .hasSize(max = USERNAME_MAX_LENGTH)
            validate(UserCreateRequest::description)
                .hasSize(max = DESCRIPTION_MAX_LENGTH)
        }
    }
}
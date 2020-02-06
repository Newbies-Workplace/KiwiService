package pl.teamkiwi.model.request

import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import pl.teamkiwi.repository.constants.DESCRIPTION_MAX_LENGTH
import pl.teamkiwi.repository.constants.EMAIL_MAX_LENGTH
import pl.teamkiwi.repository.constants.PASSWORD_MAX_LENGTH
import pl.teamkiwi.repository.constants.USERNAME_MAX_LENGTH

class UserCreateRequest(
    val email: String,
    val username: String,
    val description: String?,
    val password: String
) {

    init {
        validate(this) {
            validate(UserCreateRequest::email)
                .isNotBlank()
                .isEmail()
                .hasSize(max = EMAIL_MAX_LENGTH)
            validate(UserCreateRequest::username)
                .isNotBlank()
                .hasSize(max = USERNAME_MAX_LENGTH)
            validate(UserCreateRequest::password)
                .isNotBlank()
                .hasSize(max = PASSWORD_MAX_LENGTH)
            validate(UserCreateRequest::description)
                .hasSize(max = DESCRIPTION_MAX_LENGTH)
        }
    }
}
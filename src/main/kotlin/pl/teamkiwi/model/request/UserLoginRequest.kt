package pl.teamkiwi.model.request

import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import pl.teamkiwi.repository.constants.EMAIL_MAX_LENGTH
import pl.teamkiwi.repository.constants.PASSWORD_MAX_LENGTH

data class UserLoginRequest(
    val email: String,
    val password: String
) {

    init {
        validate(this) {
            validate(UserLoginRequest::email)
                .isNotBlank()
                .isEmail()
                .hasSize(max = EMAIL_MAX_LENGTH)
            validate(UserLoginRequest::password)
                .isNotBlank()
                .hasSize(max = PASSWORD_MAX_LENGTH)
        }
    }
}
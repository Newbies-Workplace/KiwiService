package pl.teamkiwi.model.request

import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

class UserCreateRequest(
    val email: String,
    val username: String,
    val description: String?,
    val password: String
) {
    init {
        //todo catch exception and throw custom message
        validate(this) {
            validate(UserCreateRequest::email).isNotBlank().isEmail()
            validate(UserCreateRequest::username).isNotBlank()
            validate(UserCreateRequest::password).isNotBlank()
        }
    }
}
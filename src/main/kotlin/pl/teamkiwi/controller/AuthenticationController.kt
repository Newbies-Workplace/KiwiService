package pl.teamkiwi.controller

import pl.teamkiwi.converter.toUserPrincipal
import pl.teamkiwi.exception.UnauthorizedException
import pl.teamkiwi.model.request.UserLoginRequest
import pl.teamkiwi.security.PasswordEncoder
import pl.teamkiwi.service.UserService
import pl.teamkiwi.session.AuthSession
import pl.teamkiwi.session.UserPrincipal
import java.util.*

class AuthenticationController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(userLoginRequest: UserLoginRequest): AuthSession {
        val foundUser = userService.findByEmail(userLoginRequest.email) ?: throw UnauthorizedException()
        val isValidPassword = passwordEncoder.isValid(userLoginRequest.password, foundUser.passwordHash)
        if (!isValidPassword) { throw UnauthorizedException() }

        return AuthSession(
            foundUser.id
        )
    }

    fun validate(session: AuthSession): UserPrincipal? =
        session.userId.let {
            try {
                userService.findById(UUID.fromString(it))
            } catch (e: Exception) {
                null
            }
        }?.toUserPrincipal()

    companion object {
        const val AUTH_SESSION_KEY = "Authorization"
    }
}
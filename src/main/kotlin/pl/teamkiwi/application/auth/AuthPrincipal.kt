package pl.teamkiwi.application.auth

import io.ktor.auth.Principal

data class AuthPrincipal(
    val userId: String
) : Principal
package pl.teamkiwi.session

import io.ktor.auth.Principal

data class UserPrincipal(
    val id: String,
    val email: String,
    val username: String
) : Principal
package pl.teamkiwi.util

import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import pl.teamkiwi.auth.AuthPrincipal
import pl.teamkiwi.exception.InvalidUUIDException
import java.util.*

fun ApplicationCall.idParameter(): UUID =
    try {
        UUID.fromString(parameters["id"])
    } catch (e: Throwable) {
        throw InvalidUUIDException()
    }

fun ApplicationCall.authPrincipal(): AuthPrincipal? =
    authentication.principal()
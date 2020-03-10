package pl.teamkiwi.util

import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import io.ktor.request.receive
import pl.teamkiwi.auth.AuthPrincipal
import pl.teamkiwi.exception.BadRequestException

suspend inline fun <reified T : Any> ApplicationCall.safeReceive(): T? =
    try {
        receive()
    } catch (e: Exception) {
        null
    }

/**
 * @param exception exception to throw, when receive fails
 */
suspend inline fun <reified T : Any> ApplicationCall.myReceive(
    exception: Exception = BadRequestException()
): T =
    try {
        receive()
    } catch (e: Exception) {
        throw exception
    }

fun ApplicationCall.idParameter(): String =
        parameters["id"] ?: throw BadRequestException()

fun ApplicationCall.authPrincipal(): AuthPrincipal? =
    authentication.principal()
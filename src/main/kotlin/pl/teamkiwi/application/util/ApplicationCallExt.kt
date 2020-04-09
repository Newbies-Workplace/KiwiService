package pl.teamkiwi.application.util

import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import io.ktor.http.content.PartData
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import pl.teamkiwi.application.auth.AuthPrincipal
import pl.teamkiwi.domain.model.exception.BadRequestException
import pl.teamkiwi.domain.model.exception.UnauthorizedException

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

fun ApplicationCall.fileNameParameter(): String =
    parameters["fileName"] ?: throw BadRequestException()

fun ApplicationCall.authPrincipal(): AuthPrincipal =
    authentication.principal() ?: throw UnauthorizedException()

suspend fun ApplicationCall.receiveMultipartMap(): MutableMap<String, PartData> =
    runCatching {
        receiveMultipart().toMap()
    }.getOrElse {
        throw BadRequestException()
    }
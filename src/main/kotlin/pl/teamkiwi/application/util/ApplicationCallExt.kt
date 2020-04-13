package pl.teamkiwi.application.util

import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import io.ktor.http.content.PartData
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import pl.teamkiwi.application.auth.AuthPrincipal
import pl.teamkiwi.domain.model.exception.BadRequestException
import pl.teamkiwi.domain.model.exception.UnauthorizedException
import pl.teamkiwi.domain.model.util.DEFAULT_PAGINATION
import pl.teamkiwi.domain.model.util.Pagination

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

/**
 * limit is used as n (like per_page)
 * offset is used as n * limit (like page number)
 */
fun ApplicationCall.queryPagination(): Pagination {
    val limit = request.queryParameters["limit"]?.toIntOrNull() ?: DEFAULT_PAGINATION.limit
    val offset = request.queryParameters["offset"]?.toIntOrNull()?.times(limit) ?: DEFAULT_PAGINATION.offset

    return Pagination(
        limit = limit,
        offset = offset
    )
}

suspend fun ApplicationCall.receiveMultipartMap(): MutableMap<String, PartData> =
    runCatching {
        receiveMultipart().toMap()
    }.getOrElse {
        throw BadRequestException()
    }
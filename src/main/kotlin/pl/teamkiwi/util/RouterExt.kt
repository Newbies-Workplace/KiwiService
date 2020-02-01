package pl.teamkiwi.util

import io.ktor.application.ApplicationCall
import io.ktor.request.receive
import pl.teamkiwi.exception.InvalidUUIDException
import java.util.*

suspend inline fun <reified T : Any> ApplicationCall.safeReceiveOrNull(): T? =
    try {
        receive()
    } catch (e:Exception) {
        null
    }

fun ApplicationCall.idParameter(): UUID =
    try {
        UUID.fromString(parameters["id"])
    } catch (e: Throwable) {
        throw InvalidUUIDException()
    }
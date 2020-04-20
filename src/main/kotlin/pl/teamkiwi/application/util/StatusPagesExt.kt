package pl.teamkiwi.application.util

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

inline fun <reified T : Throwable> StatusPages.Configuration.exception(code: HttpStatusCode) {
    exception(T::class.java) {
        val response = ExceptionResponse(
            code.value,
            it.message ?: "No message provided."
        )

        it.printStackTrace()

        call.respond(code, response)
    }
}

data class ExceptionResponse(
    val code: Int,
    val message: String
)
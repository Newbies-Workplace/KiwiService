package pl.teamkiwi.application.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

inline fun <reified T> deserializeOrNull(value: String): T? =
    try {
        jacksonObjectMapper().readValue<T>(value)
    } catch (e: Exception) {
        null
    }

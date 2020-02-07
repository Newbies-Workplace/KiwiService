@file:Suppress("EXPERIMENTAL_API_USAGE")

package pl.teamkiwi.util

import io.ktor.application.Application
import io.ktor.config.ApplicationConfig
import org.koin.core.scope.Scope
import org.koin.ktor.ext.get

fun Application.getPropOrNull(key: String) =
    get<ApplicationConfig>()
        .getStringPropertyOrNull(key)

fun Scope.getPropOrNull(key: String) =
    get<ApplicationConfig>()
        .getStringPropertyOrNull(key)

fun ApplicationConfig.getStringPropertyOrNull(key: String) =
    this.propertyOrNull(key)
        ?.getString()
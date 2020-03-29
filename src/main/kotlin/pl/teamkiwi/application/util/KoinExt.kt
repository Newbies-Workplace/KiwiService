@file:Suppress("EXPERIMENTAL_API_USAGE")

package pl.teamkiwi.application.util

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

fun Application.getProp(key: String) =
    get<ApplicationConfig>()
        .getStringProperty(key)

fun Scope.getProp(key: String) =
    get<ApplicationConfig>()
        .getStringProperty(key)

fun Scope.getPropList(key: String): List<String> = get<ApplicationConfig>().property(key).getList()

private fun ApplicationConfig.getStringPropertyOrNull(key: String) =
    propertyOrNull(key)?.getString()

private fun ApplicationConfig.getStringProperty(key: String) =
    property(key).getString()
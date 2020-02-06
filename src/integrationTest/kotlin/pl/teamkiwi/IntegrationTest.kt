package pl.teamkiwi

import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication

fun <R> withMyApplication(test: TestApplicationEngine.() -> R) =
    withTestApplication({ mainModule() }) {
        test()
    }
package pl.teamkiwi

import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication

//todo skip validation in tests?
fun <R> withMyApplication(test: TestApplicationEngine.() -> R) =
    withTestApplication({
        mainModule()
        testModule()
    }) {
        test()
    }
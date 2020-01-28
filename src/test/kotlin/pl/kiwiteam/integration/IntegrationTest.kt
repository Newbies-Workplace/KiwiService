package pl.kiwiteam.integration

import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import kotlin.test.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import pl.kiwiteam.di.module
import pl.kiwiteam.mainModule

abstract class IntegrationTest {

    @BeforeTest
    fun before() {
        startKoin {
            modules(module)
        }
    }

    @AfterTest
    fun after() {
        stopKoin()
    }

    fun <R> withMyApplication(test: TestApplicationEngine.() -> R) =
        withTestApplication({ mainModule() }) {
            test()
        }
}
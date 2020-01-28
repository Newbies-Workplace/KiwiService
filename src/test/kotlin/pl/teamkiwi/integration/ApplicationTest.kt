package pl.teamkiwi.integration

import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest : IntegrationTest() {

    @Test
    fun `get root should respond with hello`(): Unit = withMyApplication() {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("Hello Kiwi World", response.content)
        }
    }
}

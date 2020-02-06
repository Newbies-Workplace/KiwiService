package pl.teamkiwi

import io.ktor.http.HttpMethod
import io.ktor.server.testing.handleRequest
import org.junit.Test
import kotlin.test.assertEquals

class UserIntegrationTest {

    @Test
    fun `main request should return text`() {
        withMyApplication {
            with(handleRequest(HttpMethod.Get, "/")) {
                assertEquals("Nothing here :o", response.content)
            }
        }
    }
}
package pl.teamkiwi.authentication

import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.teamkiwi.apiLogin
import pl.teamkiwi.apiPostUser
import pl.teamkiwi.apiRequestLogout
import pl.teamkiwi.controller.createTestUserCreateRequest
import pl.teamkiwi.model.request.UserLoginRequest
import pl.teamkiwi.withMyApplication

class LogoutTest {

    @Test
    fun `logout should return Ok`() {
        withMyApplication {
            val email = "email@ok.ok"
            val password = "password"
            val loginRequest = UserLoginRequest(email, password)

            apiPostUser(createTestUserCreateRequest(email = email, password = password))

            val sessionId = apiLogin(loginRequest)

            with(apiRequestLogout(sessionId)) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `logout without sessionId should return Unauthorized`() {
        withMyApplication {
            with(apiRequestLogout()) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `logout with invalid sessionId should return Unauthorized`() {
        withMyApplication {
            with(apiRequestLogout("invalidSessionId")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}
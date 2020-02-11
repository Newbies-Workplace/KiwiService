package pl.teamkiwi.authentication

import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import pl.teamkiwi.apiLogin
import pl.teamkiwi.apiPostUser
import pl.teamkiwi.apiRequestLogin
import pl.teamkiwi.controller.createTestUserCreateRequest
import pl.teamkiwi.model.request.UserLoginRequest
import pl.teamkiwi.withMyApplication

class LoginTest {

    @Test
    fun `valid login should return session id in Authorization header`() {
        withMyApplication {
            val email = "valid@ok.ok"
            val password = "validPassword"
            val loginRequest = UserLoginRequest(email, password)

            apiPostUser(createTestUserCreateRequest(email = email, password = password))

            with(apiRequestLogin(loginRequest)) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertNotNull(response.headers["Authorization"])
                assertNotEquals("", response.headers["Authorization"])
            }
        }
    }

    @Test
    fun `invalid login should return Unauthorized`() {
        withMyApplication {
            val email = "validEmail@ok.ok"
            val validPassword = "validPassword"
            val invalidPassword = "invalidPassword"
            val loginRequest = UserLoginRequest(email, invalidPassword)

            apiPostUser(createTestUserCreateRequest(email = email, password = validPassword))

            with(apiRequestLogin(loginRequest)) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `login non existing user should return Unauthorized`() {
        withMyApplication {
            val email = "nonexisting@no.no"
            val password = "validPassword"
            val loginRequest = UserLoginRequest(email, password)

            with(apiRequestLogin(loginRequest)) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `two logged users should have different session id`() {
        withMyApplication {
            //given
            val firstEmail = "first@ok.ok"
            val secondEmail = "second@ok.ok"
            val password = "password"

            apiPostUser(createTestUserCreateRequest(email = firstEmail, password = password))
            apiPostUser(createTestUserCreateRequest(email = secondEmail, password = password))

            val firstLoginRequest = UserLoginRequest(firstEmail, password)
            val secondLoginRequest = UserLoginRequest(secondEmail, password)

            //when
            val firstSessionId = apiLogin(firstLoginRequest)
            val secondSessionId = apiLogin(secondLoginRequest)

            //then
            assertNotEquals(firstSessionId, secondSessionId)
        }
    }
}
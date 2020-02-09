package pl.teamkiwi.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.withMyApplication
import java.util.stream.Stream

class UserCreateTest {

    @ParameterizedTest
    @MethodSource("invalidUserCreateRequestProvider")
    fun `invalid body should return bad request`(body: TestUserRequest) {
        withMyApplication {
            with(handleRequest(HttpMethod.Post, "/v1/user") {
                addHeader("Content-Type", "application/json")
                setBody(ObjectMapper().writeValueAsString(body))
            }) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @ParameterizedTest
    @MethodSource("validUserCreateRequestProvider")
    fun `valid body should return created`(body: TestUserRequest) {
        withMyApplication {
            with(handleRequest(HttpMethod.Post, "/v1/user") {
                addHeader("Content-Type", "application/json")
                setBody(ObjectMapper().writeValueAsString(body))
            }) {
                val userResponse = jacksonObjectMapper().readValue<UserResponse>(response.content!!)

                assertEquals(HttpStatusCode.Created, response.status())
                assertEquals(body.email, userResponse.email)
                assertEquals(body.username, userResponse.username)
                assertEquals(body.description, userResponse.description)
            }
        }
    }

    @Test
    fun `occupied email should return conflict`() {
        withMyApplication {
            with(handleRequest(HttpMethod.Post, "/v1/user") {
                addHeader("Content-Type", "application/json")
                setBody(jacksonObjectMapper().writeValueAsString(validUserRequest))
            }) {
                assertEquals(HttpStatusCode.Created, response.status())
            }

            with(handleRequest(HttpMethod.Post, "/v1/user") {
                addHeader("Content-Type", "application/json")
                setBody(jacksonObjectMapper().writeValueAsString(validUserRequest))
            }) {
                assertEquals(HttpStatusCode.Conflict, response.status())
            }
        }
    }

    companion object {

        @JvmStatic
        @Suppress("unused") // used as method source
        private fun invalidUserCreateRequestProvider() =
            Stream.of(
                TestUserRequest(),
                TestUserRequest("not an email", "username", null, "pass"),
                TestUserRequest("email @ha.ha", "username", "description", "pass"),
                TestUserRequest("email@ha.ha", "username", "description", null),
                TestUserRequest("email@ha.ha", null, "description", "pass"),
                TestUserRequest("email@ha.ha", "", "description", "pass")
            )

        @JvmStatic
        @Suppress("unused") // used as method source
        private fun validUserCreateRequestProvider() =
            Stream.of(
                TestUserRequest("validEmail1@ok.ok", "KiwiUser", null, "password"),
                TestUserRequest("validEmail2@ok.ok", "KiwiUser", "not a null description", "password1")
            )

        private val validUserRequest =
            TestUserRequest("validEmail@ok.ok", "KiwiUser", null, "password")
    }

    data class TestUserRequest(
        val email: String? = null,
        val username: String? = null,
        val description: String? = null,
        val password: String? = null
    )
}
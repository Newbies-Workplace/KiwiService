package pl.teamkiwi.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import pl.teamkiwi.apiRequestPostUser
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.withMyApplication
import java.util.*
import java.util.stream.Stream

class UserCreateTest {

    @ParameterizedTest
    @MethodSource("invalidUserCreateRequestProvider")
    fun `invalid body should return bad request`(body: TestUserRequest) {
        withMyApplication {
            with(apiRequestPostUser(body)) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @ParameterizedTest
    @MethodSource("validUserCreateRequestProvider")
    fun `valid body should return created`(body: TestUserRequest) {
        withMyApplication {
            with(apiRequestPostUser(body)) {
                assertEquals(HttpStatusCode.Created, response.status())

                val userResponse = jacksonObjectMapper().readValue<UserResponse>(response.content!!)

                assertEquals(body.username, userResponse.username)
                assertEquals(body.description, userResponse.description)
            }
        }
    }

    @Test
    fun `occupied user id should return conflict`() {
        val token = UUID.randomUUID().toString()

        withMyApplication() {
            with(apiRequestPostUser(validUserRequest, token)) {
                assertEquals(HttpStatusCode.Created, response.status())
            }

            with(apiRequestPostUser(validUserRequest, token)) {
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
                TestUserRequest("", null),
                TestUserRequest(null, "description"),
                TestUserRequest("", "description")
            )

        @JvmStatic
        @Suppress("unused") // used as method source
        private fun validUserCreateRequestProvider() =
            Stream.of(
                TestUserRequest("KiwiUser", null),
                TestUserRequest("KiwiUser", "not a null description")
            )

        private val validUserRequest =
            TestUserRequest("KiwiUser", null)
    }

    data class TestUserRequest(
        val username: String? = null,
        val description: String? = null
    )
}
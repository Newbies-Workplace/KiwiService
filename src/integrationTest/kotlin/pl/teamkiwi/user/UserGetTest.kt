package pl.teamkiwi.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import pl.teamkiwi.apiPostUser
import pl.teamkiwi.apiRequestGetUserById
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.model.response.UserResponse
import pl.teamkiwi.withMyApplication
import java.util.stream.Stream
import kotlin.test.assertEquals

class UserGetTest {

    @Test
    fun `invalid id should return bad request`(){
        withMyApplication {
            val invalidId = "not UUID"

            with(apiRequestGetUserById(invalidId)){
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `valid uuid should return not found when user not exists`(){
        withMyApplication {
            val validId = "a1ce5228-53f8-4f34-8762-998935fa82c2"

            with(apiRequestGetUserById(validId)){
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @ParameterizedTest
    @MethodSource("validUserCreateRequestProvider")
    fun `valid uuid should return valid user`(body: UserCreateTest.TestUserRequest){
        withMyApplication {

            val userCreated = apiPostUser(
                UserCreateRequest(
                    body.email!!,
                    body.username!!,
                    body.description,
                    body.password!!
                )
            )

            with(apiRequestGetUserById(userCreated.id)){
                assertEquals(HttpStatusCode.OK, response.status())

                val userReceived = jacksonObjectMapper().readValue<UserResponse>(response.content!!)

                assertEquals(body.email, userReceived.email)
                assertEquals(body.description, userReceived.description)
                assertEquals(body.username, userReceived.username)
            }
        }
    }

    companion object {
        @JvmStatic
        @Suppress("unused") // used as method source
        private fun validUserCreateRequestProvider() =
            Stream.of(
                UserCreateTest.TestUserRequest("validE1@ok.ok", "KiwiUser", null, "password"),
                UserCreateTest.TestUserRequest("validE2@ok.ok", "KiwiUser", "not a null description", "password1")
            )
    }
}
package pl.teamkiwi.controller

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import pl.teamkiwi.exception.UnauthorizedException
import pl.teamkiwi.model.request.UserLoginRequest
import pl.teamkiwi.security.PasswordEncoder
import pl.teamkiwi.service.UserService
import pl.teamkiwi.session.AuthSession
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerTest {

    private val passwordEncoder = mockk<PasswordEncoder>()
    private val userService = mockk<UserService>()
    private val authenticationController = AuthenticationController(userService, passwordEncoder)

    @Nested
    inner class Login {

        @Test
        fun `login with valid email and password should return session`() {
            //given
            val validEmail = "email@ok.ok"
            val validPassword = "password"
            val loginRequest = UserLoginRequest(validEmail, validPassword)
            val userDTO = createTestUser(email = validEmail)

            every { userService.findByEmail(validEmail) } returns userDTO
            every { passwordEncoder.isValid(validPassword, any()) } returns true

            //when
            val session = authenticationController.login(loginRequest)

            //then
            assertEquals(userDTO.id, session.userId)
        }

        @Test
        fun `login with invalid password should throw UnauthorizedException`() {
            //given
            val validEmail = "email@ok.ok"
            val validPassword = "password"
            val loginRequest = UserLoginRequest(validEmail, validPassword)
            val userDTO = createTestUser(email = validEmail)

            every { userService.findByEmail(validEmail) } returns userDTO
            every { passwordEncoder.isValid(validPassword, any()) } returns false

            //when
            assertThrows<UnauthorizedException> {
                authenticationController.login(loginRequest)
            }
        }

        @Test
        fun `login with invalid email should throw UnauthorizedException`() {
            //given
            val validEmail = "email@ok.ok"
            val validPassword = "password"
            val loginRequest = UserLoginRequest(validEmail, validPassword)

            every { userService.findByEmail(validEmail) } returns null

            //when
            assertThrows<UnauthorizedException> {
                authenticationController.login(loginRequest)
            }
        }
    }

    @Nested
    inner class Validate {

        @Test
        fun `validate with valid id should return principal`() {
            //given
            val userId = "b92a22b7-390b-44c9-ae0f-afa09bcdf86a"
            val testUser = createTestUser(id = userId)
            val session = AuthSession(userId)

            every { userService.findById(UUID.fromString(userId)) } returns testUser

            //when
            val principal = authenticationController.validate(session)

            //then
            assertNotNull(principal)
            assertEquals(testUser.id, principal?.id)
            assertEquals(testUser.email, principal?.email)
            assertEquals(testUser.username, principal?.username)
        }

        @Test
        fun `validate with invalid id should return null`() {
            //given
            val session = AuthSession("invalidUUID")

            //when
            val principal = authenticationController.validate(session)

            //then
            assertNull(principal)
        }
    }
}
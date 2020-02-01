package pl.teamkiwi.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import pl.teamkiwi.exception.EmailOccupiedException
import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.service.UserService
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserControllerTest {

    @Nested
    inner class CreateUser {

        private val userService = mockk<UserService>()
        private val userController = UserController(userService)

        @Test
        fun `should not create user if email is occupied`() {
            //given
            val validEmail = "email@co.ok"
            val validUser = createTestUser(email = validEmail)
            val userCreateRequest = createTestUserCreateRequest(email = validEmail)

            every { userService.findByEmail(validEmail) } returns validUser

            //when
            assertThrows<EmailOccupiedException> {
                userController.createUser(userCreateRequest)
            }
        }

        @Test
        fun `should save user when email is free`() {
            //given
            val userCreateRequest = createTestUserCreateRequest()
            val validUser = mockk<UserDTO>()

            every { userService.save(any()) } returns validUser
            every { userService.findByEmail(any()) } returns null

            //when
            userController.createUser(userCreateRequest)

            //then
            verify(exactly = 1) { userService.save(userCreateRequest) }
        }
    }

}

fun createTestUserCreateRequest(
    email: String = "email@co.pl",
    username: String = "UserName",
    description: String? = null,
    password: String = "ahbsiboa"
) = UserCreateRequest(
    email, username, description, password
)

fun createTestUser(
    id: String = "67b44030-4448-11ea-b77f-2e728ce88125",
    email: String = "email@co.pl",
    username: String = "UserName",
    description: String? = null,
    passwordHash: String = "ahbsiboa",
    avatarPath: String? = null,
    creationDate: Date = Date()
) = UserDTO(
    id, email, username, description, avatarPath, passwordHash, creationDate
)
package pl.teamkiwi.controller

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import pl.teamkiwi.converter.toUserResponse
import pl.teamkiwi.exception.AccountAlreadyExistsException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.dto.UserDTO
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.service.UserService
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserControllerTest {

    private val userService = mockk<UserService>()
    private val userController = UserController(userService)

    @Nested
    inner class PostUser {

        @Test
        fun `should create user when valid data passed`() {
            //given
            val id = UUID.randomUUID()
            val idString = id.toString()
            val userCreateRequest = createTestUserCreateRequest()

            val testUser = createTestUser(
                idString,
                userCreateRequest.username,
                userCreateRequest.description)

            every { userService.findById(id) } returns null
            every { userService.save(id, any()) } returns testUser

            //when
            val foundUser = userController.createUser(idString, userCreateRequest)

            //then
            assertEquals(testUser.toUserResponse(), foundUser)
        }

        @Test
        fun `should throw AccountAlreadyExistsException when user with same id exists`() {
            //given
            val id = UUID.randomUUID()
            val idString = id.toString()
            val userCreateRequest = createTestUserCreateRequest()

            every { userService.findById(id) } returns mockk()

            //when
            assertThrows<AccountAlreadyExistsException> {
                userController.createUser(idString, userCreateRequest)
            }
        }
    }

    @Nested
    inner class GetUser {

        @Test
        fun `should return user when found with specified id`() {
            //given
            val validUserResponse = createTestUser()
            val validId = mockk<UUID>()

            every { userService.findById(validId) } returns validUserResponse

            //when
            val foundUser = userController.getUserById(validId)

            //then
            assertEquals(validUserResponse.toUserResponse(), foundUser)
        }

        @Test
        fun `should throw NotFoundException when id doesn't match`(){
            //given
            val validId = mockk<UUID>()

            //when
            every { userService.findById(validId) } returns null

            //then
            assertThrows<NotFoundException> {
                userController.getUserById(validId)
            }
        }
    }
}

fun createTestUserCreateRequest(
    email: String = "email@co.pl",
    username: String = "UserName"
) = UserCreateRequest(
    email, username
)

fun createTestUser(
    id: String = "67b44030-4448-11ea-b77f-2e728ce88125",
    username: String = "UserName",
    description: String? = null,
    avatarPath: String? = null,
    creationDate: Date = Date()
) = UserDTO(
    id, username, description, avatarPath, creationDate
)
package pl.teamkiwi.domain.service

import io.mockk.every
import io.mockk.mockk
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import pl.teamkiwi.application.model.request.UserCreateRequest
import pl.teamkiwi.domain.model.entity.User
import pl.teamkiwi.domain.model.exception.AccountAlreadyExistsException
import pl.teamkiwi.infrastructure.repository.exposed.UserExposedRepository

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserServiceTest {

    private val userRepository = mockk<UserExposedRepository>()
    private val userController = UserService(userRepository)

    @Nested
    inner class CreateUser {

        @Test
        fun `should create user when valid data passed`() {
            //given
            val id = "validId"
            val userCreateRequest = createTestUserCreateRequest()

            val testUser = createTestUser(
                id,
                userCreateRequest.username,
                userCreateRequest.description
            )

            every { userRepository.findById(id) } returns null
            every { userRepository.save(any()) } returns testUser

            //when
            val foundUser = userController.createUser(id, userCreateRequest)

            //then
            assertEquals(testUser, foundUser)
        }

        @Test
        fun `should throw AccountAlreadyExistsException when user with same id exists`() {
            //given
            val id = "validId"
            val userCreateRequest = createTestUserCreateRequest()

            every { userRepository.findById(id) } returns mockk()

            //when
            assertThrows<AccountAlreadyExistsException> {
                userController.createUser(id, userCreateRequest)
            }
        }
    }

    @Nested
    inner class GetUser {

        @Test
        fun `should return user when found with specified id`() {
            //given
            val validUserResponse = createTestUser()
            val id = "validId"

            every { userRepository.findById(id) } returns validUserResponse

            //when
            val foundUser = userController.getUserById(id)

            //then
            assertEquals(validUserResponse, foundUser)
        }
    }
}

fun createTestUserCreateRequest(
    username: String = "xXxusernamexXx",
    description: String? = null
) = UserCreateRequest(
    username = username,
    description = description
)

fun createTestUser(
    id: String = "67b44030-4448-11ea-b77f-2e728ce88125",
    username: String = "UserName",
    description: String? = null,
    creationDate: DateTime = DateTime.now()
) = User(
    id = id,
    username = username,
    description = description,
    creationDate = creationDate
)
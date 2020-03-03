package pl.teamkiwi

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.junit.jupiter.api.Assertions.assertEquals
import pl.teamkiwi.controller.createTestUserCreateRequest
import pl.teamkiwi.model.request.UserCreateRequest
import java.util.*

fun TestApplicationEngine.apiPostUser(user: UserCreateRequest = createTestUserCreateRequest()) =
    with(apiRequestPostUser(user)) {
        assertEquals(HttpStatusCode.Created, response.status())
    }

fun TestApplicationEngine.apiRequestPostUser(body: Any, authToken: String? = UUID.randomUUID().toString()) =
    postRequest("/v1/user", body, authToken)

private fun TestApplicationEngine.postRequest(uri: String, body: Any, authToken: String?) =
    handleRequest(HttpMethod.Post, uri) {
        addHeader("Content-Type", "application/json")
        authToken?.let { addHeader("Authorization", it) }
        setBody(jacksonObjectMapper().writeValueAsString(body))
    }
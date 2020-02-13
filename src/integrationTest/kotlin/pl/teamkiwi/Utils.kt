package pl.teamkiwi

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.junit.jupiter.api.Assertions.*
import pl.teamkiwi.controller.createTestUserCreateRequest
import pl.teamkiwi.model.request.UserCreateRequest
import pl.teamkiwi.model.request.UserLoginRequest
import pl.teamkiwi.model.response.UserResponse

fun TestApplicationEngine.apiGetUserById(id: String) =
    with(apiRequestGetUserById(id)){
        assertEquals(HttpStatusCode.OK, response.status())

        jacksonObjectMapper().readValue<UserResponse>(response.content!!)
    }

fun TestApplicationEngine.apiPostUser(user: UserCreateRequest = createTestUserCreateRequest()) =
    with(apiRequestPostUser(user)) {
        assertEquals(HttpStatusCode.Created, response.status())

        jacksonObjectMapper().readValue<UserResponse>(response.content!!)
    }

fun TestApplicationEngine.apiLogin(loginRequest: UserLoginRequest) =
    with(apiRequestLogin(loginRequest)) {
        val sessionId = response.headers["Authorization"]

        assertEquals(HttpStatusCode.OK, response.status())
        assertNotNull(response.headers["Authorization"])
        assertNotEquals("", sessionId)

        sessionId!!
    }

fun TestApplicationEngine.apiRequestGetUserById(id: String) = getRequest("/v1/user/$id")

fun TestApplicationEngine.apiRequestLogin(body: Any) =
    postRequest("/v1/login", body)

fun TestApplicationEngine.apiRequestLogout(sessionId: String? = null) =
    handleRequest(HttpMethod.Get, "/v1/logout") {
        sessionId?.let { addHeader("Authorization", it) }
    }

fun TestApplicationEngine.apiRequestPostUser(body: Any) =
    postRequest("/v1/user", body)


private fun TestApplicationEngine.postRequest(uri: String, body: Any) =
    handleRequest(HttpMethod.Post, uri) {
        addHeader("Content-Type", "application/json")
        setBody(jacksonObjectMapper().writeValueAsString(body))
    }

private fun TestApplicationEngine.getRequest(uri: String) =
    handleRequest(HttpMethod.Get, uri) {
        addHeader("Content-Type", "application/json")
    }

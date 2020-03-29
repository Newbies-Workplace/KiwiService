package pl.teamkiwi.application.router

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject
import pl.teamkiwi.application.controller.UserController
import pl.teamkiwi.application.util.idParameter

fun Routing.userRoutes() {
    val userController: UserController by inject()

    authenticate {
        post("/v1/user") {
            userController.postUser(call)
        }

        get("/v1/users") {
            userController.getAllUsers(call)
        }

        get("/v1/user/{id}") {
            val id = call.idParameter()

            userController.getUserById(call, id)
        }
    }
}
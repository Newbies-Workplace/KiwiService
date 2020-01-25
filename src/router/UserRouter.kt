package pl.kiwiteam.router

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.koin.ktor.ext.inject
import pl.kiwiteam.controller.UserController

fun Routing.userRoutes() {
    val userController: UserController by inject()

    get("/") {
        call.respond(userController.sayHello())
    }

    get("/json/gson") {
        call.respond(mapOf("hello" to "world"))
    }
}
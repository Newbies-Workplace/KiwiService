package pl.teamkiwi.controller

import pl.teamkiwi.service.UserService

class UserController (
    private val userService: UserService
) {

    fun sayHello(): String {
        return userService.sayHello()
    }
}
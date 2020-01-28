package pl.kiwiteam.controller

import pl.kiwiteam.service.UserService

class UserController (
    private val userService: UserService
) {

    fun sayHello(): String {
        return userService.sayHello()
    }
}
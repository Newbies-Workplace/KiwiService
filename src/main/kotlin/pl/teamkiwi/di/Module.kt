package pl.teamkiwi.di

import org.koin.dsl.module
import pl.teamkiwi.controller.UserController
import pl.teamkiwi.service.UserService

val module = module {
    single { UserController(get()) }

    single { UserService() }
}
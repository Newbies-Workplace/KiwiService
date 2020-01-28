package pl.kiwiteam.di

import org.koin.dsl.module
import pl.kiwiteam.controller.UserController
import pl.kiwiteam.service.UserService

val module = module {
    single { UserController(get()) }

    single { UserService() }
}
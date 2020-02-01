package pl.teamkiwi.di

import com.typesafe.config.ConfigFactory
import io.ktor.config.ApplicationConfig
import io.ktor.config.HoconApplicationConfig
import org.koin.dsl.module
import pl.teamkiwi.controller.UserController
import pl.teamkiwi.repository.UserRepository
import pl.teamkiwi.service.UserService

val module = module {
    @Suppress("EXPERIMENTAL_API_USAGE")
    single { HoconApplicationConfig(ConfigFactory.load()) as ApplicationConfig }

    single { UserController(get()) }

    single { UserService(get()) }
}

val repositoryModule = module {
    single { UserRepository() }
}
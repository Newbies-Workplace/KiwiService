package pl.teamkiwi.di

import com.typesafe.config.ConfigFactory
import io.ktor.config.ApplicationConfig
import io.ktor.config.HoconApplicationConfig
import org.koin.dsl.module
import pl.teamkiwi.controller.UserController
import pl.teamkiwi.converter.UserConverter
import pl.teamkiwi.repository.DatabaseConfiguration
import pl.teamkiwi.repository.Exposed
import pl.teamkiwi.repository.UserRepository
import pl.teamkiwi.security.PasswordEncoder
import pl.teamkiwi.service.UserService
import pl.teamkiwi.util.getPropOrNull

val module = module {
    @Suppress("EXPERIMENTAL_API_USAGE")
    single { HoconApplicationConfig(ConfigFactory.load()) as ApplicationConfig }
    single { PasswordEncoder() }

    single { UserConverter(get()) }

    single { UserController(get(), get()) }

    single { UserService(get()) }
}

val repositoryModule = module {
    single { UserRepository() }

    single { Exposed() }

    single {
        DatabaseConfiguration(
            url = getPropOrNull("kiwi.database.url")!!,
            driver = getPropOrNull("kiwi.database.driver")!!,
            user = getPropOrNull("kiwi.database.user")!!,
            password = getPropOrNull("kiwi.database.password")!!)
    }
}
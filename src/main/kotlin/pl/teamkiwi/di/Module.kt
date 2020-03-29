package pl.teamkiwi.di

import com.typesafe.config.ConfigFactory
import io.ktor.config.ApplicationConfig
import io.ktor.config.HoconApplicationConfig
import org.koin.dsl.module
import pl.jutupe.DatabaseConfiguration
import pl.teamkiwi.application.controller.AlbumController
import pl.teamkiwi.application.controller.SongController
import pl.teamkiwi.application.controller.UserController
import pl.teamkiwi.application.util.getProp
import pl.teamkiwi.application.util.getPropList
import pl.teamkiwi.domain.`interface`.AlbumRepository
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.`interface`.UserRepository
import pl.teamkiwi.domain.service.AlbumService
import pl.teamkiwi.domain.service.FileService
import pl.teamkiwi.domain.service.SongService
import pl.teamkiwi.domain.service.UserService
import pl.teamkiwi.infrastructure.repository.AlbumExposedRepository
import pl.teamkiwi.infrastructure.repository.SongExposedRepository
import pl.teamkiwi.infrastructure.repository.UserExposedRepository

val module = module {
    @Suppress("EXPERIMENTAL_API_USAGE")
    single { HoconApplicationConfig(ConfigFactory.load()) as ApplicationConfig }

    single { UserController(get()) }
    single { UserService(get()) }
    single { UserExposedRepository() as UserRepository }

    single { SongController(get(), get()) }
    single { SongService(get(), get(), get()) }
    single { SongExposedRepository() as SongRepository }

    single { AlbumController(get(), get()) }
    single { AlbumService(get(), get()) }
    single { AlbumExposedRepository() as AlbumRepository }

    single { DatabaseConfiguration(
        url = getProp("kiwi.database.url"),
        driver = getProp("kiwi.database.driver"),
        user = getProp("kiwi.database.user"),
        password = getProp("kiwi.database.password"))
    }

    single { FileService(get()) }
    single {
        FileService.Configuration(
            songPath = getProp("kiwi.storage.path.song"),
            imagePath = getProp("kiwi.storage.path.image"),
            songExtensions = getPropList("kiwi.storage.extension.song"),
            imageExtensions = getPropList("kiwi.storage.extension.image")
        )
    }
}
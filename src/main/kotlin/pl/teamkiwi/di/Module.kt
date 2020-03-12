package pl.teamkiwi.di

import com.typesafe.config.ConfigFactory
import io.ktor.config.ApplicationConfig
import io.ktor.config.HoconApplicationConfig
import org.koin.dsl.module
import pl.jutupe.DatabaseConfiguration
import pl.teamkiwi.controller.AlbumController
import pl.teamkiwi.controller.SongController
import pl.teamkiwi.controller.UserController
import pl.teamkiwi.repository.AlbumRepository
import pl.teamkiwi.repository.SongRepository
import pl.teamkiwi.repository.UserRepository
import pl.teamkiwi.service.AlbumService
import pl.teamkiwi.service.FileService
import pl.teamkiwi.service.SongService
import pl.teamkiwi.service.UserService
import pl.teamkiwi.util.getProp
import pl.teamkiwi.util.getPropList

val module = module {
    @Suppress("EXPERIMENTAL_API_USAGE")
    single { HoconApplicationConfig(ConfigFactory.load()) as ApplicationConfig }

    single { UserController(get()) }
    single { UserService(get()) }
    single { UserRepository() }

    single { SongController(get(), get()) }
    single { SongService(get()) }
    single { SongRepository() }

    single { AlbumController(get(), get()) }
    single { AlbumService(get()) }
    single { AlbumRepository() }

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
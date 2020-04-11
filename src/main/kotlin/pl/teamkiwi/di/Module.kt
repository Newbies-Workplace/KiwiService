package pl.teamkiwi.di

import com.typesafe.config.ConfigFactory
import io.ktor.config.ApplicationConfig
import io.ktor.config.HoconApplicationConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pl.jutupe.DatabaseConfiguration
import pl.teamkiwi.application.controller.AlbumController
import pl.teamkiwi.application.controller.FileController
import pl.teamkiwi.application.controller.SongController
import pl.teamkiwi.application.controller.UserController
import pl.teamkiwi.application.converter.AlbumConverter
import pl.teamkiwi.application.converter.SongConverter
import pl.teamkiwi.application.util.DownloadPathProvider
import pl.teamkiwi.application.util.getProp
import pl.teamkiwi.application.util.getPropList
import pl.teamkiwi.domain.`interface`.AlbumRepository
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.`interface`.UserRepository
import pl.teamkiwi.domain.service.AlbumService
import pl.teamkiwi.domain.service.SongService
import pl.teamkiwi.domain.service.UserService
import pl.teamkiwi.infrastructure.repository.exposed.AlbumExposedRepository
import pl.teamkiwi.infrastructure.repository.exposed.SongExposedRepository
import pl.teamkiwi.infrastructure.repository.exposed.UserExposedRepository
import pl.teamkiwi.infrastructure.repository.file.FileRepository
import pl.teamkiwi.infrastructure.repository.file.ImageFileRepository
import pl.teamkiwi.infrastructure.repository.file.SongFileRepository

val module = module {
    @Suppress("EXPERIMENTAL_API_USAGE")
    single { HoconApplicationConfig(ConfigFactory.load()) as ApplicationConfig }

    single { UserController(get()) }
    single { UserService(get()) }
    single { UserExposedRepository() as UserRepository }

    single { SongConverter(get()) }
    single { SongController(get(), get(), get(), get()) }
    single { SongService(get(), get(), get(), get()) }
    single { SongExposedRepository() as SongRepository }

    single { AlbumConverter(get(), get()) }
    single { AlbumController(get(), get(), get()) }
    single { AlbumService(get(), get()) }
    single { AlbumExposedRepository() as AlbumRepository }

    single { FileController(get(), get()) }
    single { ImageFileRepository(get(named("imageFileRepositoryConfiguration"))) }
    single { SongFileRepository(get(named("songFileRepositoryConfiguration"))) }

    single {
        DownloadPathProvider(
            serverAddress = getProp("kiwi.address")
        )
    }

    single { DatabaseConfiguration(
        url = getProp("kiwi.database.url"),
        driver = getProp("kiwi.database.driver"),
        user = getProp("kiwi.database.user"),
        password = getProp("kiwi.database.password"))
    }

    single(named("imageFileRepositoryConfiguration")) {
        FileRepository.Configuration(
            uploadPath = getProp("kiwi.storage.path.image"),
            allowedExtensions = getPropList("kiwi.storage.extension.image")
        )
    }

    single(named("songFileRepositoryConfiguration")) {
        FileRepository.Configuration(
            uploadPath = getProp("kiwi.storage.path.song"),
            allowedExtensions = getPropList("kiwi.storage.extension.song")
        )
    }
}
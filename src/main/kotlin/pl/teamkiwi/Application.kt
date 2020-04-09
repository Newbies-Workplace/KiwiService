package pl.teamkiwi

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.logger.PrintLogger
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import org.slf4j.event.Level
import pl.jutupe.Exposed
import pl.teamkiwi.application.auth.server
import pl.teamkiwi.application.router.albumRoutes
import pl.teamkiwi.application.router.fileRoutes
import pl.teamkiwi.application.router.songRoutes
import pl.teamkiwi.application.router.userRoutes
import pl.teamkiwi.application.util.getProp
import pl.teamkiwi.di.module
import pl.teamkiwi.domain.model.exception.*
import pl.teamkiwi.infrastructure.repository.exposed.table.AlbumSongs
import pl.teamkiwi.infrastructure.repository.exposed.table.Albums
import pl.teamkiwi.infrastructure.repository.exposed.table.Songs
import pl.teamkiwi.infrastructure.repository.exposed.table.Users

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        commandLineEnvironment(args)
    ).start()
}

@Suppress("unused") // Referenced in application.conf
fun Application.mainModule() {
    install(Koin) {
        logger(PrintLogger())

        modules(
            listOf(module)
        )
    }

    install(CallLogging) {
        level = Level.INFO
    }

    install(Exposed) {
        connectWithConfig(get())

        createSchemas(
            Users,
            Songs,
            Albums,
            AlbumSongs
        )
    }

    install(CORS) {
        anyHost()

        method(HttpMethod.Delete)
        method(HttpMethod.Put)
    }

    install(Authentication) {
        server {
            authServerUrl { getProp("kiwi.auth.url") }
        }
    }

    install(ContentNegotiation) {
        jackson {}
    }

    install(StatusPages) {
        exception<BadRequestException> { call.respond(HttpStatusCode.BadRequest) }
        exception<NotFoundException> { call.respond(HttpStatusCode.NotFound) }
        exception<ConflictException> { call.respond(HttpStatusCode.Conflict) }
        exception<NoContentException> { call.respond(HttpStatusCode.NoContent) }
        exception<UnauthorizedException> { call.respond(HttpStatusCode.Unauthorized) }
        exception<UnsupportedExtensionException> { call.respond(HttpStatusCode.UnsupportedMediaType) }
        exception<FileSaveException> { call.respond(HttpStatusCode.InternalServerError) }
        exception<FileDeleteException> { call.respond(HttpStatusCode.InternalServerError) }
        exception<ForbiddenException> { call.respond(HttpStatusCode.Forbidden) }
    }

    routing {
        userRoutes()
        songRoutes()
        albumRoutes()
        fileRoutes()
    }
}

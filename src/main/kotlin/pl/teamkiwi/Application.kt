package pl.teamkiwi

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
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
import pl.teamkiwi.auth.server
import pl.teamkiwi.di.module
import pl.teamkiwi.exception.*
import pl.teamkiwi.repository.Exposed
import pl.teamkiwi.router.songRoutes
import pl.teamkiwi.router.userRoutes
import pl.teamkiwi.util.getProp

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        commandLineEnvironment(args)
    ).start()
}

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
        createSchemas()
    }

    install(CORS) {
        anyHost()
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
        exception<AccountAlreadyExistsException> { call.respond(HttpStatusCode.Conflict) }
        exception<NoContentException> { call.respond(HttpStatusCode.NoContent) }
        exception<UnauthorizedException> { call.respond(HttpStatusCode.Unauthorized) }
    }

    routing {
        userRoutes()
        songRoutes()
    }
}

package pl.teamkiwi

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.session
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
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.header
import org.koin.core.logger.PrintLogger
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import org.slf4j.event.Level
import pl.teamkiwi.controller.AuthenticationController
import pl.teamkiwi.controller.AuthenticationController.Companion.AUTH_SESSION_KEY
import pl.teamkiwi.di.module
import pl.teamkiwi.di.repositoryModule
import pl.teamkiwi.exception.*
import pl.teamkiwi.repository.Exposed
import pl.teamkiwi.router.authenticationRoutes
import pl.teamkiwi.router.songRoutes
import pl.teamkiwi.router.userRoutes
import pl.teamkiwi.session.AuthSession
import java.util.*

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
            listOf(module, repositoryModule)
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
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        anyHost()
    }

    install(Sessions) {
        header<AuthSession>(AUTH_SESSION_KEY, SessionStorageMemory()) {
            identity { UUID.randomUUID().toString() }
        }
    }

    install(Authentication) {
        val authenticationController by inject<AuthenticationController>()

        session<AuthSession> {
            challenge { call.respond(HttpStatusCode.Unauthorized) }

            validate { session ->
                authenticationController.validate(session)
            }
        }
    }

    install(ContentNegotiation) {
        jackson {}
    }

    //todo extract into other class
    install(StatusPages) {
        exception<BadRequestException> { call.respond(HttpStatusCode.BadRequest) }
        exception<NotFoundException> { call.respond(HttpStatusCode.NotFound) }
        exception<EmailOccupiedException> { call.respond(HttpStatusCode.Conflict) }
        exception<NoContentException> { call.respond(HttpStatusCode.NoContent) }
        exception<UnauthorizedException> { call.respond(HttpStatusCode.Unauthorized) }
    }

    routing {
        authenticationRoutes()
        userRoutes()
        songRoutes()
    }
}

package pl.teamkiwi

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.CORS
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
import pl.teamkiwi.di.module
import pl.teamkiwi.di.repositoryModule
import pl.teamkiwi.exception.BadRequestException
import pl.teamkiwi.exception.EmailOccupiedException
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.repository.Exposed
import pl.teamkiwi.router.userRoutes

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

    install(Authentication) {}

    install(ContentNegotiation) {
        jackson {}
    }

    //todo extract into other class
    install(StatusPages) {
        exception<BadRequestException> { call.respond(HttpStatusCode.BadRequest) }
        exception<NotFoundException> { call.respond(HttpStatusCode.NotFound) }
        exception<EmailOccupiedException> { call.respond(HttpStatusCode.Conflict) }
        exception<NoContentException> { call.respond(HttpStatusCode.NoContent) }
    }

    routing {
        userRoutes()
    }
}

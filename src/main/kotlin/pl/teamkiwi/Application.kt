package pl.teamkiwi

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.gson.*
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.context.startKoin
import pl.teamkiwi.di.module
import pl.teamkiwi.router.userRoutes

fun main(args: Array<String>) {
    startKoin {
        modules(module)
    }

    embeddedServer(
        Netty,
        commandLineEnvironment(args)
    ).start()
}

@Suppress("unused") // Referenced in application.conf
fun Application.mainModule() {
    install(CORS) {
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        anyHost()
    }

    install(Authentication) {}

    install(ContentNegotiation) {
        gson {}
    }

    routing {
        userRoutes()
    }
}

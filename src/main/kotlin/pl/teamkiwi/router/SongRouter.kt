package pl.teamkiwi.router

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import pl.teamkiwi.model.request.SongCreateRequest
import pl.teamkiwi.util.myReceive

fun Routing.songRoutes() {

    authenticate {
        post("v1/song") {
            val song = call.receiveMultipart()
            val request = call.myReceive<SongCreateRequest>()

            println("name : " + song.readPart()?.name)

            call.respond(request)
        }
    }
}
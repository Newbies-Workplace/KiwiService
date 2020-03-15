package pl.teamkiwi.router

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.content.forEachPart
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject
import pl.teamkiwi.controller.SongController
import pl.teamkiwi.exception.BadRequestException
import pl.teamkiwi.exception.UnauthorizedException
import pl.teamkiwi.model.request.SongCreateRequest
import pl.teamkiwi.util.*

fun Routing.songRoutes() {
    val songController: SongController by inject()

    authenticate {
        post("v1/song") {
            val multipart = call.receiveMultipart()
            val partDataMap = runCatching { multipart.toMap() }
                    .getOrElse { throw BadRequestException() }
            val songRequestForm = partDataMap.getRequestOrNull() ?: throw BadRequestException()

            val songRequest = deserializeOrNull<SongCreateRequest>(songRequestForm.value) ?: throw BadRequestException()
            val song = partDataMap.getSongOrNull() ?: throw BadRequestException()
            val image = partDataMap.getImageOrNull()
            val userId = call.authPrincipal()?.userId ?: throw UnauthorizedException()
            val albumIdParam: String? = call.request.queryParameters["albumId"]

            val response = songController.createSong(songRequest, song, image, userId, albumIdParam)

            call.respond(response)

            multipart.forEachPart { it.dispose() }
            partDataMap.clear()
        }

        get("v1/song/{id}") {
            val id = call.idParameter()

            val response = songController.getSongById(id)

            call.respond(response)
        }

        get("v1/songs") {
            call.respond(songController.getAllSongs())
        }

        delete("v1/song/{id}") {
            val id = call.idParameter()
            val userId = call.authPrincipal()?.userId ?: throw UnauthorizedException()

            songController.deleteSong(id, userId)

            call.respond("")
        }
    }
}
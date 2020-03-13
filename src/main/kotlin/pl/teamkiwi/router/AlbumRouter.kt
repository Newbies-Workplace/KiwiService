package pl.teamkiwi.router

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.forEachPart
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject
import pl.teamkiwi.controller.AlbumController
import pl.teamkiwi.exception.BadRequestException
import pl.teamkiwi.exception.UnauthorizedException
import pl.teamkiwi.model.request.AlbumCreateRequest
import pl.teamkiwi.util.*

fun Routing.albumRoutes() {
    val albumController: AlbumController by inject()

    authenticate {
        post("v1/album") {
            val multipart = call.receiveMultipart()
            val userId = call.authPrincipal()?.userId ?: throw UnauthorizedException()

            val partDataMap = runCatching { multipart.toMap() }
                .getOrElse { throw BadRequestException() }

            val albumRequestForm = partDataMap.getRequestOrNull() ?: throw BadRequestException()

            val albumRequest = deserializeOrNull<AlbumCreateRequest>(albumRequestForm.value) ?: throw BadRequestException()
            val image = partDataMap.getImageOrNull()

            val response = albumController.createAlbum(albumRequest, image, userId)

            call.respond(HttpStatusCode.Created, response)

            multipart.forEachPart { it.dispose() }
            partDataMap.clear()
        }

        get("v1/album/{id}") {
            val id = call.idParameter()

            val response = albumController.getAlbumById(id)

            call.respond(response)
        }

        get("v1/albums") {
            call.respond(albumController.getAllAlbums())
        }

        /*delete("v1/album/{id}") {

        }

        put("v1/album/{id}/song") {

        }

        delete("v1/album/{id}/song") {

        }*/
    }
}
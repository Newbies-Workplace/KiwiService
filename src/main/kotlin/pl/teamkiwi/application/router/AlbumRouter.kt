package pl.teamkiwi.application.router

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import pl.teamkiwi.application.controller.AlbumController
import pl.teamkiwi.application.util.idParameter

fun Routing.albumRoutes() {
    val albumController: AlbumController by inject()

    authenticate {
        post("v1/album") {
            albumController.postAlbum(call)
        }

        get("v1/album/{id}") {
            val id = call.idParameter()

            albumController.getAlbumById(call, id)
        }

        get("v1/albums") {
            albumController.getAllAlbums(call)
        }

        delete("v1/album/{id}") {
            val id = call.idParameter()

            albumController.deleteAlbumById(call, id)
        }

        put("v1/album/{id}/songs") {
            val id = call.idParameter()

            albumController.putAlbumSongs(call, id)
        }

        delete("v1/album/{id}/songs") {
            val id = call.idParameter()

            albumController.deleteAlbumSongs(call, id)
        }
    }
}
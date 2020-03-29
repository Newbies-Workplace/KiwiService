package pl.teamkiwi.application.router

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject
import pl.teamkiwi.application.controller.SongController
import pl.teamkiwi.application.util.idParameter

fun Routing.songRoutes() {
    val songController: SongController by inject()

    authenticate {
        post("v1/song") {
            songController.postSong(call)
        }

        get("v1/song/{id}") {
            val id = call.idParameter()

            songController.getSongById(call, id)
        }

        get("v1/songs") {
            songController.getAllSongs(call)
        }

        delete("v1/song/{id}") {
            val id = call.idParameter()

            songController.deleteSongById(call, id)
        }
    }
}
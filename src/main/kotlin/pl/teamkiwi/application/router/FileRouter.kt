package pl.teamkiwi.application.router

import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.koin.ktor.ext.inject
import pl.teamkiwi.application.controller.FileController
import pl.teamkiwi.application.util.fileNameParameter

fun Routing.fileRoutes() {
    val fileController: FileController by inject()

    get("v1/file/image/{fileName}") {
        val imageName = call.fileNameParameter()

        fileController.getImage(call, imageName)
    }

    get("v1/file/song/{fileName}") {
        val songName = call.fileNameParameter()

        fileController.getSong(call, songName)
    }
}
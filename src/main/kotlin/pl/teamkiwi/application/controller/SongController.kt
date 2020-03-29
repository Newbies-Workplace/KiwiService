package pl.teamkiwi.application.controller

import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import pl.teamkiwi.application.model.request.SongCreateRequest
import pl.teamkiwi.application.util.*
import pl.teamkiwi.domain.model.exception.BadRequestException
import pl.teamkiwi.domain.service.FileService
import pl.teamkiwi.domain.service.SongService

class SongController(
    private val songService: SongService,
    private val fileService: FileService
) {

    suspend fun postSong(call: ApplicationCall) {
        val userId = call.authPrincipal().userId
        val partDataMap = call.receiveMultipartMap()

        val songRequest = partDataMap.getRequestOrNull<SongCreateRequest>() ?: throw BadRequestException()
        val song = partDataMap.getSongOrNull() ?: throw BadRequestException()
        val image = partDataMap.getImageOrNull()
        val albumIdParam: String? = call.request.queryParameters["albumId"]

        var songPath: String? = null
        var imagePath: String? = null

        runCatching {
            songPath = fileService.saveSong(song)
            imagePath = image?.let { fileService.saveImage(it) }

            val response = songService.createSong(songRequest, songPath!!, imagePath, userId, albumIdParam)

            call.respond(response)

            partDataMap.dispose()
        }.getOrElse { exception ->
            songPath?.let { fileService.deleteFile(it) }
            imagePath?.let { fileService.deleteFile(it) }

            partDataMap.dispose()

            //in case of rollback, we need to throw exception
            throw exception
        }
    }

    suspend fun getSongById(call: ApplicationCall, id: String) {
        val response = songService.getSongById(id)

        call.respond(response)
    }

    suspend fun getAllSongs(call: ApplicationCall) {
        call.respond(songService.getAllSongs())
    }

    suspend fun deleteSongById(call: ApplicationCall, id: String) {
        val userId = call.authPrincipal().userId

        songService.deleteSong(id, userId)

        call.respond("")
    }
}
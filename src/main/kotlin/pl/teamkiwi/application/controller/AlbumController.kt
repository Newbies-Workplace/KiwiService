package pl.teamkiwi.application.controller

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import pl.teamkiwi.application.model.request.AlbumCreateRequest
import pl.teamkiwi.application.util.*
import pl.teamkiwi.domain.model.exception.BadRequestException
import pl.teamkiwi.domain.service.AlbumService
import pl.teamkiwi.domain.service.FileService

class AlbumController (
    private val albumService: AlbumService,
    private val fileService: FileService
) {

    suspend fun postAlbum(call: ApplicationCall) {
        val userId = call.authPrincipal().userId
        val partDataMap = call.receiveMultipartMap()

        val albumRequest = partDataMap.getRequestOrNull<AlbumCreateRequest>() ?: throw BadRequestException()
        val image = partDataMap.getImageOrNull()

        var imagePath: String? = null

        runCatching {
            imagePath = image?.let { fileService.saveImage(it) }

            val response = albumService.createAlbum(albumRequest, imagePath, userId)

            call.respond(HttpStatusCode.Created, response)

            partDataMap.dispose()
        }.getOrElse { exception ->
            imagePath?.let { fileService.deleteFile(it) }

            partDataMap.dispose()

            //in case of rollback, we need to throw exception
            throw exception
        }
    }

    suspend fun getAlbumById(call: ApplicationCall, id: String) {
        val response = albumService.getAlbumById(id)

        call.respond(response)
    }

    suspend fun getAllAlbums(call: ApplicationCall) {
        call.respond(albumService.getAllAlbums())
    }

    suspend fun deleteAlbumById(call: ApplicationCall, id: String) {
        val userId = call.authPrincipal().userId

        albumService.deleteAlbum(id, userId)

        call.respond("")
    }

    suspend fun putAlbumSongs(call: ApplicationCall, id: String) {
        val userId = call.authPrincipal().userId
        val songIds = call.myReceive<List<String>>()

        albumService.addSongs(id, songIds, userId)

        call.respond("")
    }

    suspend fun deleteAlbumSongs(call: ApplicationCall, id: String) {
        val userId = call.authPrincipal().userId
        val songIds = call.myReceive<List<String>>()

        albumService.removeSongs(id, songIds, userId)

        call.respond("")
    }
}
package pl.teamkiwi.application.controller

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import pl.teamkiwi.application.converter.AlbumConverter
import pl.teamkiwi.application.model.request.AlbumCreateRequest
import pl.teamkiwi.application.util.*
import pl.teamkiwi.domain.model.entity.ImageFile
import pl.teamkiwi.domain.model.exception.BadRequestException
import pl.teamkiwi.domain.model.exception.NoContentException
import pl.teamkiwi.domain.model.exception.NotFoundException
import pl.teamkiwi.domain.service.AlbumService
import pl.teamkiwi.infrastructure.repository.file.ImageFileRepository

class AlbumController (
    private val albumService: AlbumService,
    private val imageFileRepository: ImageFileRepository,
    private val albumConverter: AlbumConverter
) {

    suspend fun postAlbum(call: ApplicationCall) {
        val userId = call.authPrincipal().userId
        val partDataMap = call.receiveMultipartMap()

        val albumRequest = partDataMap.getRequestOrNull<AlbumCreateRequest>() ?: throw BadRequestException("Error while fetching request.")
        val image = partDataMap.getImageOrNull()

        var imageFile: ImageFile? = null

        runCatching {
            imageFile = image?.let { imageFileRepository.save(it) }

            val album = albumService.createAlbum(albumRequest, imageFile, userId)
            val response = with(albumConverter) { album.toAlbumResponse() }

            call.respond(HttpStatusCode.Created, response)

            partDataMap.dispose()
        }.getOrElse { exception ->
            imageFile?.let { imageFileRepository.delete(it) }

            partDataMap.dispose()

            //in case of rollback, we need to throw exception
            throw exception
        }
    }

    suspend fun getAlbumById(call: ApplicationCall, id: String) {
        val album = albumService.getAlbumById(id) ?: throw NotFoundException("Album with given id: $id was not found.")

        val response = with(albumConverter) { album.toAlbumResponse() }

        call.respond(response)
    }

    suspend fun getAllAlbums(call: ApplicationCall) {
        val pagination = call.queryPagination()

        val albums = albumService.getAllAlbums(pagination)

        if (albums.isEmpty()) {
            throw NoContentException()
        }

        val response = with(albumConverter) {
            albums.map { it.toAlbumResponse() }
        }

        call.respond(response)
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
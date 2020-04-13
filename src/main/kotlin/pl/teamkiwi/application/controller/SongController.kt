package pl.teamkiwi.application.controller

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import pl.teamkiwi.application.converter.SongConverter
import pl.teamkiwi.application.model.request.SongCreateRequest
import pl.teamkiwi.application.util.*
import pl.teamkiwi.domain.model.entity.ImageFile
import pl.teamkiwi.domain.model.entity.SongFile
import pl.teamkiwi.domain.model.exception.BadRequestException
import pl.teamkiwi.domain.model.exception.NoContentException
import pl.teamkiwi.domain.model.exception.NotFoundException
import pl.teamkiwi.domain.service.SongService
import pl.teamkiwi.infrastructure.repository.file.ImageFileRepository
import pl.teamkiwi.infrastructure.repository.file.SongFileRepository

class SongController(
    private val songService: SongService,
    private val songFileRepository: SongFileRepository,
    private val imageFileRepository: ImageFileRepository,
    private val songConverter: SongConverter
) {

    suspend fun postSong(call: ApplicationCall) {
        val userId = call.authPrincipal().userId
        val partDataMap = call.receiveMultipartMap()

        val songRequest = partDataMap.getRequestOrNull<SongCreateRequest>() ?: throw BadRequestException("Error while fetching request.")
        val songItem = partDataMap.getSongOrNull() ?: throw BadRequestException("Error while fetching song")
        val imageItem = partDataMap.getImageOrNull()
        val albumIdParam: String? = call.request.queryParameters["albumId"]

        var songFile: SongFile? = null
        var imageFile: ImageFile? = null

        runCatching {
            songFile = songFileRepository.save(songItem)
            imageFile = imageItem?.let { imageFileRepository.save(it) }

            val song = songService.createSong(songRequest, songFile!!, imageFile, userId, albumIdParam)
            val response = with(songConverter) { song.toSongResponse() }

            call.respond(HttpStatusCode.Created, response)

            partDataMap.dispose()
        }.getOrElse { exception ->
            songFile?.let { songFileRepository.delete(it) }
            imageFile?.let { imageFileRepository.delete(it) }

            partDataMap.dispose()

            //in case of rollback, we need to throw exception
            throw exception
        }
    }

    suspend fun getSongById(call: ApplicationCall, id: String) {
        val song = songService.getSongById(id) ?: throw NotFoundException("Song with given id: $id was not found.")

        val response = with(songConverter) { song.toSongResponse() }

        call.respond(response)
    }

    suspend fun getAllSongs(call: ApplicationCall) {
        val pagination = call.queryPagination()

        val songs = songService.getAllSongs(pagination)

        if (songs.isEmpty()) {
            throw NoContentException()
        }

        val response = with(songConverter) {
            songs.map { it.toSongResponse() }
        }

        call.respond(response)
    }

    suspend fun deleteSongById(call: ApplicationCall, id: String) {
        val userId = call.authPrincipal().userId

        songService.deleteSong(id, userId)

        call.respond("")
    }
}
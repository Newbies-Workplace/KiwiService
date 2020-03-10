package pl.teamkiwi.controller

import io.ktor.http.content.PartData
import pl.teamkiwi.converter.toSongCreateDTO
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.dto.SongDTO
import pl.teamkiwi.model.request.SongCreateRequest
import pl.teamkiwi.service.FileService
import pl.teamkiwi.service.SongService

class SongController(
    private val songService: SongService,
    private val fileService: FileService
) {

    suspend fun createSong(
        songRequest: SongCreateRequest,
        song: PartData.FileItem,
        image: PartData.FileItem?,
        userId: String
    ): SongDTO {
        var songPath: String? = null
        var imagePath: String? = null

        runCatching {
            songPath = fileService.saveSong(song)
            imagePath = image?.let { fileService.saveImage(it) }

            val songCreateDTO = songRequest.toSongCreateDTO(
                    songPath!!,
                    imagePath,
                    userId,
                    100L) //todo extract song duration


            return songService.save(songCreateDTO)
        }.getOrElse { exception ->
            songPath?.let { fileService.deleteFile(it) }
            imagePath?.let { fileService.deleteFile(it) }

            //in case of rollback, we need to throw exception
            throw exception
        }
    }

    fun getSongById(id: String): SongDTO =
        songService.findById(id) ?: throw NotFoundException()

    fun getAllSongs(): List<SongDTO> {
        val songs = songService.findAll()

        if (songs.isEmpty()) {
            throw NoContentException()
        }

        return songs
    }
}
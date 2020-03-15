package pl.teamkiwi.controller

import io.ktor.http.content.PartData
import pl.teamkiwi.converter.toSongCreateDTO
import pl.teamkiwi.exception.ForbiddenException
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.dto.SongDTO
import pl.teamkiwi.model.request.SongCreateRequest
import pl.teamkiwi.service.AlbumService
import pl.teamkiwi.service.FileService
import pl.teamkiwi.service.SongService

class SongController(
    private val songService: SongService,
    private val albumService: AlbumService,
    private val fileService: FileService
) {

    suspend fun createSong(
        songRequest: SongCreateRequest,
        song: PartData.FileItem,
        image: PartData.FileItem?,
        userId: String,
        albumId: String? = null
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
                100L //todo extract song duration
            )

            val createdSong = songService.save(songCreateDTO)

            //add song to album if albumId is not null
            albumId?.let { id ->
                val album = albumService.findById(id) ?: throw NotFoundException()

                if (album.artistId != userId) {
                    throw ForbiddenException()
                }

                albumService.addSongs(id, listOf(createdSong.id))
            }

            return createdSong
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

    fun deleteSong(id: String, userId: String) {
        val song = songService.findById(id) ?: throw NotFoundException()

        if (userId != song.artistId) {
            throw ForbiddenException()
        }

        fileService.deleteFile(song.path)
        song.imagePath?.let { fileService.deleteFile(it) }

        songService.deleteById(id)
    }
}
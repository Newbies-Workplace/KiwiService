package pl.teamkiwi.controller

import io.ktor.http.content.PartData
import pl.teamkiwi.converter.toAlbumCreateDTO
import pl.teamkiwi.exception.BadRequestException
import pl.teamkiwi.exception.ForbiddenException
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.dto.AlbumDTO
import pl.teamkiwi.model.request.AlbumCreateRequest
import pl.teamkiwi.service.AlbumService
import pl.teamkiwi.service.FileService
import pl.teamkiwi.service.SongService

class AlbumController(
    private val albumService: AlbumService,
    private val songService: SongService,
    private val fileService: FileService
) {

    suspend fun createAlbum(
        albumCreateRequest: AlbumCreateRequest,
        image: PartData.FileItem?,
        userId: String
    ): AlbumDTO {
        var imagePath: String? = null

        runCatching {
            imagePath = image?.let { fileService.saveImage(it) }

            val albumCreateDTO = albumCreateRequest.toAlbumCreateDTO(
                userId,
                imagePath
            )

            return albumService.save(albumCreateDTO)
        }.getOrElse { exception ->
            imagePath?.let { fileService.deleteFile(it) }

            throw exception
        }
    }

    fun getAlbumById(id: String): AlbumDTO =
        albumService.findById(id) ?: throw NotFoundException()

    fun getAllAlbums(): List<AlbumDTO> {
        val albums = albumService.findAll()

        if (albums.isEmpty()) {
            throw NoContentException()
        }

        return albums
    }

    fun deleteAlbum(albumId: String, userId: String) {
        //check permission for album
        assertAlbumPermission(userId, albumId)

        albumService.deleteById(albumId)
    }

    fun addSongs(albumId: String, songIds: List<String>, userId: String) {
        //check permission for album
        assertAlbumPermission(userId, albumId)

        //check permission for all songs
        songIds.forEach { songId -> assertSongPermission(userId, songId) }

        //check if song is not already in any album
        songIds.forEach {
            val song = songService.findById(it)

            if (song?.albumId != null) {
                throw BadRequestException()
            }
        }

        //check if songIds is distinct
        if (songIds.size != songIds.distinct().size) {
            throw BadRequestException()
        }

        albumService.addSongs(albumId, songIds)
    }

    fun removeSongs(albumId: String, songIds: List<String>, userId: String) {
        //check permission for album
        assertAlbumPermission(userId, albumId)

        //check permission for all songs
        songIds.forEach { songId -> assertSongPermission(userId, songId) }

        //check if songIds is distinct
        if (songIds.size != songIds.distinct().size) {
            throw BadRequestException()
        }

        albumService.removeSongs(songIds)
    }

    private fun assertAlbumPermission(userId: String, albumId: String) {
        val album = albumService.findById(albumId) ?: throw NotFoundException()

        if (album.artistId != userId) {
            throw ForbiddenException()
        }
    }

    private fun assertSongPermission(userId: String, songId: String) {
        val song = songService.findById(songId) ?: throw NotFoundException()

        if (song.artistId != userId) {
            throw ForbiddenException()
        }
    }
}
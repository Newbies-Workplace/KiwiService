package pl.teamkiwi.controller

import io.ktor.http.content.PartData
import pl.teamkiwi.converter.toAlbumCreateDTO
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.dto.AlbumDTO
import pl.teamkiwi.model.request.AlbumCreateRequest
import pl.teamkiwi.service.AlbumService
import pl.teamkiwi.service.FileService

class AlbumController(
    private val albumService: AlbumService,
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
}
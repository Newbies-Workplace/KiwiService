package pl.teamkiwi.controller

import io.ktor.http.content.PartData
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import pl.teamkiwi.exception.ForbiddenException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.dto.AlbumDTO
import pl.teamkiwi.model.request.AlbumCreateRequest
import pl.teamkiwi.service.AlbumService
import pl.teamkiwi.service.FileService
import pl.teamkiwi.service.SongService
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AlbumControllerTest {

    private val albumService = mockk<AlbumService>()
    private val songService = mockk<SongService>()
    private val fileService = mockk<FileService>()
    private val albumController = AlbumController(albumService, songService, fileService)

    @Nested
    inner class CreateAlbum {

        @Test
        fun `should delete files when exception is thrown`() {
            //given
            val albumCreateRequest = AlbumCreateRequest("album title")
            val imageFile = mockk<PartData.FileItem>()
            val userId = "baee8a8c-2c3e-4e71-b85e-9264e0a563d3" //random UUID
            val imagePath = "songPath.mp3"

            coEvery { fileService.saveImage(imageFile) } returns imagePath
            every { fileService.deleteFile(any()) } just runs

            //when
            assertThrows<Exception> {
                runBlocking {
                    albumController.createAlbum(albumCreateRequest, imageFile, userId)
                }
            }

            //then
            verify(exactly = 1) { fileService.deleteFile(imagePath) }
        }

        @Test
        fun `should return album when valid data provided`() {
            //given
            val albumCreateRequest = AlbumCreateRequest("album title")
            val imageFile = mockk<PartData.FileItem>()
            val albumDTO = mockk<AlbumDTO>()
            val userId = "baee8a8c-2c3e-4e71-b85e-9264e0a563d3" //random UUID
            val imagePath = "songPath.mp3"

            coEvery { fileService.saveImage(imageFile) } returns imagePath
            every { albumService.save(any()) } returns albumDTO


            //when
            val song = runBlocking {
                albumController.createAlbum(albumCreateRequest, imageFile, userId)
            }

            //then
            assertEquals(albumDTO, song)
            verify(exactly = 0) { fileService.deleteFile(any()) }
        }
    }

    @Nested
    inner class GetAlbum {

        @Test
        fun `should throw NotFoundException when there is no album with specific id`() {
            //given
            val albumId = "albumId"
            every { albumService.findById(albumId) } returns null

            //when
            assertThrows<NotFoundException> {
                albumController.getAlbumById(albumId)
            }
        }

        @Test
        fun `should return album when found with specified id`() {
            //given
            val albumId = "albumId"
            val albumDTO = createTestAlbum(albumId)
            every { albumService.findById(albumId) } returns albumDTO

            //when
            val response = albumController.getAlbumById(albumId)

            //then
            assertEquals(albumDTO, response)
        }
    }

    @Nested
    inner class DeleteAlbum {
        //todo after refactor
    }

    @Nested
    inner class AddSongs {

        @Test
        fun `should throw NotFoundException when there is no song with given id`() {
            //given
            val albumId = "albumId"
            val firstSongId = "songId"
            val songIds = listOf(firstSongId)
            val userId = "userId"
            val album = createTestAlbum(
                id = albumId,
                artistId = userId
            )

            every { albumService.findById(albumId) } returns album
            every { songService.findById(firstSongId) } returns null

            //when
            assertThrows<NotFoundException> {
                albumController.addSongs(albumId, songIds, userId)
            }
        }

        @Test
        fun `should throw ForbiddenException when there is song not created by user`() {
            //given
            val albumId = "albumId"
            val firstSongId = "songId"
            val songIds = listOf(firstSongId)
            val userId = "userId"
            val album = createTestAlbum(
                id = albumId,
                artistId = userId
            )
            val song = createTestSong(
                id = firstSongId,
                artistId = "notUserId"
            )

            every { albumService.findById(albumId) } returns album
            every { songService.findById(firstSongId) } returns song

            //when
            assertThrows<ForbiddenException> {
                albumController.addSongs(albumId, songIds, userId)
            }
        }
    }

    @Nested
    inner class RemoveSongs {

        @Test
        fun `should throw NotFoundException when there is no song with given id`() {
            //given
            val albumId = "albumId"
            val firstSongId = "songId"
            val songIds = listOf(firstSongId)
            val userId = "userId"
            val album = createTestAlbum(
                id = albumId,
                artistId = userId
            )

            every { albumService.findById(albumId) } returns album
            every { songService.findById(firstSongId) } returns null

            //when
            assertThrows<NotFoundException> {
                albumController.removeSongs(albumId, songIds, userId)
            }
        }

        @Test
        fun `should throw ForbiddenException when there is song not created by user`() {
            //given
            val albumId = "albumId"
            val firstSongId = "songId"
            val songIds = listOf(firstSongId)
            val userId = "userId"
            val album = createTestAlbum(
                id = albumId,
                artistId = userId
            )
            val song = createTestSong(
                id = firstSongId,
                artistId = "notUserId"
            )

            every { albumService.findById(albumId) } returns album
            every { songService.findById(firstSongId) } returns song

            //when
            assertThrows<ForbiddenException> {
                albumController.removeSongs(albumId, songIds, userId)
            }
        }
    }
}

fun createTestAlbum(
    id: String = "songId",
    title: String = "SongTitle",
    imagePath: String? = null,
    artistId: String = "artistRandomId",
    uploadDate: Date = Date()
) =
    AlbumDTO(
        id = id,
        title = title,
        artistId = artistId,
        imagePath = imagePath,
        uploadDate = uploadDate
    )
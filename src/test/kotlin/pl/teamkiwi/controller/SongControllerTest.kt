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
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.dto.SongDTO
import pl.teamkiwi.model.request.SongCreateRequest
import pl.teamkiwi.service.AlbumService
import pl.teamkiwi.service.FileService
import pl.teamkiwi.service.SongService
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class SongControllerTest {

    private val songService = mockk<SongService>()
    private val albumService = mockk<AlbumService>()
    private val fileService = mockk<FileService>()
    private val songController = SongController(songService, albumService, fileService)

    @Nested
    inner class CreateSong {

        @Test
        fun `should delete files when exception is thrown`() {
            //given
            val songCreateRequest = SongCreateRequest("title")
            val songFile = mockk<PartData.FileItem>()
            val imageFile = mockk<PartData.FileItem>()
            val userId = "baee8a8c-2c3e-4e71-b85e-9264e0a563d3" //random UUID
            val songPath = "songPath.mp3"

            coEvery { fileService.saveSong(songFile) } returns songPath
            coEvery { fileService.saveImage(imageFile) } throws Exception()
            every { fileService.deleteFile(any()) } just runs

            //when
            assertThrows<Exception> {
                runBlocking {
                    songController.createSong(songCreateRequest, songFile, imageFile, userId)
                }
            }

            //then
            verify(exactly = 1) { fileService.deleteFile(songPath) }
        }

        @Test
        fun `should return song when valid data provided`() {
            //given
            val songCreateRequest = SongCreateRequest("title")
            val songFile = mockk<PartData.FileItem>()
            val songDTO = mockk<SongDTO>()
            val userId = "baee8a8c-2c3e-4e71-b85e-9264e0a563d3" //random UUID
            val songPath = "songPath.mp3"

            coEvery { fileService.saveSong(songFile) } returns songPath
            every { songService.save(any()) } returns songDTO


            //when
            val song = runBlocking {
                songController.createSong(songCreateRequest, songFile, null, userId)
            }

            //then
            assertEquals(songDTO, song)
            verify(exactly = 0) { fileService.deleteFile(any()) }
        }

        @Test
        fun `should throw NotFoundException when there is no album with specified albumId`() {
            //given
            val songCreateRequest = SongCreateRequest("title")
            val songFile = mockk<PartData.FileItem>()
            val userId = "baee8a8c-2c3e-4e71-b85e-9264e0a563d3" //random UUID
            val albumId = "albumId"

            coEvery { fileService.saveSong(songFile) } returns "anyString"
            every { songService.save(any()) } returns mockk()
            every { albumService.findById(albumId) } returns null
            every { fileService.deleteFile(any()) } just runs

            //when
            assertThrows<NotFoundException> {
                runBlocking {
                    songController.createSong(songCreateRequest, songFile, null, userId, albumId)
                }
            }
        }

        @Test
        fun `should throw ForbiddenException when album is not posted by user`() {
            //given
            val songCreateRequest = SongCreateRequest("title")
            val songFile = mockk<PartData.FileItem>()
            val userId = "firstUserId"
            val albumId = "albumId"
            val album = createTestAlbum(id = albumId, artistId = "secondUserId")

            coEvery { fileService.saveSong(songFile) } returns "anyString"
            every { songService.save(any()) } returns mockk()
            every { albumService.findById(albumId) } returns album
            every { fileService.deleteFile(any()) } just runs

            //when
            assertThrows<ForbiddenException> {
                runBlocking {
                    songController.createSong(songCreateRequest, songFile, null, userId, albumId)
                }
            }
        }
    }

    @Nested
    inner class GetSongById {

        @Test
        fun `should throw NotFoundException when there is no song with specific id`() {
            //given
            val id = "validId"
            every { songService.findById(id) } returns null

            //when
            assertThrows<NotFoundException>{
                songController.getSongById(id)
            }
        }

        @Test
        fun `should return song response when valid id passed`() {
            //given
            val id = "validId"
            val songDTO = mockk<SongDTO>()
            every { songService.findById(id) } returns songDTO

            //when
            val song = songController.getSongById(id)

            //then
            assertEquals(songDTO, song)
        }
    }

    @Nested
    inner class GetAllSongs {

        @Test
        fun `should throw NoContentException when song list is empty`() {
            //given
            every { songService.findAll() } returns emptyList()

            //when
            assertThrows<NoContentException> {
                songController.getAllSongs()
            }
        }
    }

    @Nested
    inner class DeleteSong {

        @Test
        fun `should throw NotFoundException when there is no song with specific id`() {
            //given
            val userId = "userId"
            val songId = "songId"

            every { songService.findById(songId) } returns null

            //when
            assertThrows<NotFoundException> {
                songController.deleteSong(songId, userId)
            }
        }

        @Test
        fun `should throw ForbiddenException when another user wants to delete not his song`() {
            //given
            val firstUserId = "firstUserId"
            val secondUserId = "secondUserId"
            val songId = "songId"
            val song = createTestSong(id = songId, artistId = firstUserId)

            every { songService.findById(songId) } returns song

            //when
            assertThrows<ForbiddenException> {
                songController.deleteSong(songId, secondUserId)
            }
        }
    }
}

fun createTestSong(
    id: String = "songId",
    title: String = "SongTitle",
    imagePath: String? = null,
    artistId: String = "artistRandomId",
    albumId: String? = null,
    path: String = "path/song.mp3",
    duration: Long = 100L,
    uploadDate: Date = Date()
) =
    SongDTO(
        id = id,
        title = title,
        imagePath = imagePath,
        artistId = artistId,
        albumId = albumId,
        path = path,
        duration = duration,
        uploadDate = uploadDate
    )
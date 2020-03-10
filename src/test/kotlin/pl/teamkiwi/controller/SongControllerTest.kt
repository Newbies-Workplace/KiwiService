package pl.teamkiwi.controller

import io.ktor.http.content.PartData
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import pl.teamkiwi.exception.NoContentException
import pl.teamkiwi.exception.NotFoundException
import pl.teamkiwi.model.dto.SongDTO
import pl.teamkiwi.model.request.SongCreateRequest
import pl.teamkiwi.service.FileService
import pl.teamkiwi.service.SongService

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class SongControllerTest {

    private val songService = mockk<SongService>()
    private val fileService = mockk<FileService>()
    private val songController = SongController(songService, fileService)

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
}
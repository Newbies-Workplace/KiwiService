package pl.teamkiwi.controller

import io.ktor.http.content.PartData
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import pl.teamkiwi.model.dto.AlbumDTO
import pl.teamkiwi.model.request.AlbumCreateRequest
import pl.teamkiwi.service.AlbumService
import pl.teamkiwi.service.FileService

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AlbumControllerTest {

    private val albumService = mockk<AlbumService>()
    private val fileService = mockk<FileService>()
    private val albumController = AlbumController(albumService, fileService)

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
            Assertions.assertEquals(albumDTO, song)
            verify(exactly = 0) { fileService.deleteFile(any()) }
        }
    }
}
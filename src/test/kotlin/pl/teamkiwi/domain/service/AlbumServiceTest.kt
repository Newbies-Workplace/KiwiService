package pl.teamkiwi.domain.service

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import pl.teamkiwi.application.model.request.AlbumCreateRequest
import pl.teamkiwi.domain.`interface`.AlbumRepository
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.model.entity.Album
import pl.teamkiwi.domain.model.entity.ImageFile
import pl.teamkiwi.domain.model.exception.ForbiddenException
import pl.teamkiwi.domain.model.exception.NotFoundException

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AlbumServiceTest {

    private val albumRepository = mockk<AlbumRepository>()
    private val songRepository = mockk<SongRepository>()
    private val albumService = AlbumService(albumRepository, songRepository)

    @Nested
    inner class CreateAlbum {

        @Test
        fun `should return album when valid data provided`() {
            //given
            val albumCreateRequest = AlbumCreateRequest("album title")
            val albumDTO = mockk<Album>()
            val userId = "baee8a8c-2c3e-4e71-b85e-9264e0a563d3" //random UUID
            val imageFile = ImageFile("image.jpg")

            every { albumRepository.save(any()) } returns albumDTO

            //when
            val song = runBlocking {
                albumService.createAlbum(albumCreateRequest, imageFile, userId)
            }

            //then
            assertEquals(albumDTO, song)
        }
    }

    @Nested
    inner class GetAlbum {

        @Test
        fun `should return album when found with specified id`() {
            //given
            val albumId = "albumId"
            val albumDTO = createTestAlbum(albumId)
            every { albumRepository.findById(albumId) } returns albumDTO

            //when
            val response = albumService.getAlbumById(albumId)

            //then
            assertEquals(albumDTO, response)
        }
    }

    @Nested
    inner class DeleteAlbum {

        @Test
        fun `should throw NotFoundException when there is no album with specific id`() {
            //given
            val userId = "userId"
            val albumId = "albumId"

            every { albumRepository.findById(albumId) } returns null

            //when
            assertThrows<NotFoundException> {
                albumService.deleteAlbum(albumId, userId)
            }
        }

        @Test
        fun `should throw ForbiddenException when another user wants to delete not his album`() {
            //given
            val firstUserId = "firstUserId"
            val secondUserId = "secondUserId"
            val albumId = "albumId"
            val album =
                createTestAlbum(id = albumId, artistId = firstUserId)

            every { albumRepository.findById(albumId) } returns album

            //when
            assertThrows<ForbiddenException> {
                albumService.deleteAlbum(albumId, secondUserId)
            }
        }
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

            every { albumRepository.findById(albumId) } returns album
            every { songRepository.findById(firstSongId) } returns null

            //when
            assertThrows<NotFoundException> {
                albumService.addSongs(albumId, songIds, userId)
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

            every { albumRepository.findById(albumId) } returns album
            every { songRepository.findById(firstSongId) } returns song

            //when
            assertThrows<ForbiddenException> {
                albumService.addSongs(albumId, songIds, userId)
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

            every { albumRepository.findById(albumId) } returns album
            every { songRepository.findById(firstSongId) } returns null

            //when
            assertThrows<NotFoundException> {
                albumService.removeSongs(albumId, songIds, userId)
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

            every { albumRepository.findById(albumId) } returns album
            every { songRepository.findById(firstSongId) } returns song

            //when
            assertThrows<ForbiddenException> {
                albumService.removeSongs(albumId, songIds, userId)
            }
        }
    }
}

fun createTestAlbum(
    id: String = "songId",
    title: String = "SongTitle",
    imageFile: ImageFile? = null,
    artistId: String = "artistRandomId",
    uploadDate: DateTime = DateTime.now()
) =
    Album(
        id = id,
        title = title,
        artistId = artistId,
        imageFile = imageFile,
        uploadDate = uploadDate
    )
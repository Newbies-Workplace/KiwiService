package pl.teamkiwi.domain.service

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import pl.teamkiwi.application.model.request.SongCreateRequest
import pl.teamkiwi.domain.`interface`.AlbumRepository
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.model.entity.ImageFile
import pl.teamkiwi.domain.model.entity.Song
import pl.teamkiwi.domain.model.entity.SongFile
import pl.teamkiwi.domain.model.exception.ForbiddenException
import pl.teamkiwi.domain.model.exception.NotFoundException
import pl.teamkiwi.domain.model.util.DEFAULT_PAGINATION
import pl.teamkiwi.infrastructure.repository.file.ImageFileRepository
import pl.teamkiwi.infrastructure.repository.file.SongFileRepository

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class SongServiceTest {

    private val songRepository = mockk<SongRepository>()
    private val albumRepository = mockk<AlbumRepository>()
    private val imageFileRepository = mockk<ImageFileRepository>()
    private val songFileRepository = mockk<SongFileRepository>()
    private val songService = SongService(songRepository, albumRepository, imageFileRepository, songFileRepository)

    @Nested
    inner class CreateSong {

        @Test
        fun `should return song when valid data provided`() {
            //given
            val songCreateRequest = SongCreateRequest("title")
            val songDTO = mockk<Song>()
            val userId = "baee8a8c-2c3e-4e71-b85e-9264e0a563d3" //random UUID
            val songFile = SongFile("songPath.mp3")

            every { songRepository.save(any()) } returns songDTO

            //when
            val song = runBlocking {
                songService.createSong(songCreateRequest, songFile, null, userId)
            }

            //then
            assertEquals(songDTO, song)
        }

        @Test
        fun `should throw NotFoundException when there is no album with specified albumId`() {
            //given
            val songCreateRequest = SongCreateRequest("title")
            val songFile = SongFile("songPath.mp3")
            val userId = "baee8a8c-2c3e-4e71-b85e-9264e0a563d3" //random UUID
            val albumId = "albumId"

            every { songRepository.save(any()) } returns mockk()
            every { albumRepository.findById(albumId) } returns null

            //when
            assertThrows<NotFoundException> {
                runBlocking {
                    songService.createSong(songCreateRequest, songFile, null, userId, albumId)
                }
            }
        }

        @Test
        fun `should throw ForbiddenException when album is not posted by user`() {
            //given
            val songCreateRequest = SongCreateRequest("title")
            val songFile = SongFile("songPath.mp3")
            val userId = "firstUserId"
            val albumId = "albumId"
            val album = createTestAlbum(id = albumId, artistId = "secondUserId")

            every { songRepository.save(any()) } returns mockk()
            every { albumRepository.findById(albumId) } returns album
            every { imageFileRepository.delete(any()) } just runs
            every { songFileRepository.delete(any()) } just runs

            //when
            assertThrows<ForbiddenException> {
                runBlocking {
                    songService.createSong(songCreateRequest, songFile, null, userId, albumId)
                }
            }
        }
    }

    @Nested
    inner class GetSongById {

        @Test
        fun `should return song response when valid id passed`() {
            //given
            val id = "validId"
            val songDTO = mockk<Song>()
            every { songRepository.findById(id) } returns songDTO

            //when
            val song = songService.getSongById(id)

            //then
            assertEquals(songDTO, song)
        }
    }

    @Nested
    inner class GetAllSongs {

        @Test
        fun `should return all songs`() {
            //given
            val song = mockk<Song>()
            every { songRepository.findAll(DEFAULT_PAGINATION) } returns listOf(song)

            //when
            val songs = songService.getAllSongs(DEFAULT_PAGINATION)

            //then
            assertEquals(listOf(song), songs)
        }
    }

    @Nested
    inner class DeleteSong {

        @Test
        fun `should throw NotFoundException when there is no song with specific id`() {
            //given
            val userId = "userId"
            val songId = "songId"

            every { songRepository.findById(songId) } returns null

            //when
            assertThrows<NotFoundException> {
                songService.deleteSong(songId, userId)
            }
        }

        @Test
        fun `should throw ForbiddenException when another user wants to delete not his song`() {
            //given
            val firstUserId = "firstUserId"
            val secondUserId = "secondUserId"
            val songId = "songId"
            val song = createTestSong(id = songId, artistId = firstUserId)

            every { songRepository.findById(songId) } returns song

            //when
            assertThrows<ForbiddenException> {
                songService.deleteSong(songId, secondUserId)
            }
        }
    }
}

fun createTestSong(
    id: String = "songId",
    title: String = "SongTitle",
    imageFile: ImageFile? = null,
    artistId: String = "artistRandomId",
    albumId: String? = null,
    songFile: SongFile = SongFile("song.mp3"),
    duration: Long = 100L,
    uploadDate: DateTime = DateTime.now()
) =
    Song(
        id = id,
        title = title,
        imageFile = imageFile,
        artistId = artistId,
        albumId = albumId,
        file = songFile,
        duration = duration,
        uploadDate = uploadDate
    )
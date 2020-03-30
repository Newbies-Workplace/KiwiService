package pl.teamkiwi.domain.service

import org.joda.time.DateTime
import pl.teamkiwi.application.model.request.SongCreateRequest
import pl.teamkiwi.domain.`interface`.AlbumRepository
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.model.entity.Song
import pl.teamkiwi.domain.model.exception.ForbiddenException
import pl.teamkiwi.domain.model.exception.NoContentException
import pl.teamkiwi.domain.model.exception.NotFoundException
import java.util.*

class SongService(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val fileService: FileService
) {

    fun createSong(
        songRequest: SongCreateRequest,
        songPath: String,
        imagePath: String?,
        userId: String,
        albumId: String? = null
    ): Song {
        val songId = UUID.randomUUID().toString()

        val song = Song(
            id = songId,
            title = songRequest.title,
            path = songPath,
            imagePath = imagePath,
            artistId = userId,
            duration = 100L, //todo extract song duration,
            albumId = albumId,
            uploadDate = DateTime.now()
        )

        val savedSong = songRepository.save(song)

        //add song to album if albumId is not null
        albumId?.let { id ->
            val album = albumRepository.findById(id) ?: throw NotFoundException()

            if (album.artistId != userId) {
                throw ForbiddenException()
            }

            albumRepository.addSongs(id, listOf(songId))
        }

        return savedSong
    }

    fun getSongById(id: String): Song =
        songRepository.findById(id) ?: throw NotFoundException()

    fun getAllSongs(): List<Song> {
        val songs = songRepository.findAll()

        if (songs.isEmpty()) {
            throw NoContentException()
        }

        return songs
    }

    fun deleteSong(id: String, userId: String) {
        val song = songRepository.findById(id) ?: throw NotFoundException()

        if (userId != song.artistId) {
            throw ForbiddenException()
        }

        fileService.deleteFile(song.path)
        song.imagePath?.let { fileService.deleteFile(it) }

        songRepository.deleteById(id)
    }
}
package pl.teamkiwi.domain.service

import org.joda.time.DateTime
import pl.teamkiwi.application.model.request.SongCreateRequest
import pl.teamkiwi.domain.`interface`.AlbumRepository
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.model.entity.ImageFile
import pl.teamkiwi.domain.model.entity.Song
import pl.teamkiwi.domain.model.entity.SongFile
import pl.teamkiwi.domain.model.exception.ForbiddenException
import pl.teamkiwi.domain.model.exception.NotFoundException
import pl.teamkiwi.domain.model.util.Pagination
import pl.teamkiwi.infrastructure.repository.file.ImageFileRepository
import pl.teamkiwi.infrastructure.repository.file.SongFileRepository
import java.util.*

class SongService(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val imageFileRepository: ImageFileRepository,
    private val songFileRepository: SongFileRepository
) {

    fun createSong(
        songRequest: SongCreateRequest,
        songFile: SongFile,
        imageFile: ImageFile?,
        userId: String,
        albumId: String? = null
    ): Song {
        val songId = UUID.randomUUID().toString()

        val song = Song(
            id = songId,
            title = songRequest.title,
            file = songFile,
            imageFile = imageFile,
            artistId = userId,
            duration = 100L, //todo extract song duration,
            albumId = albumId,
            uploadDate = DateTime.now()
        )

        val savedSong = songRepository.save(song)

        //add song to album if albumId is not null
        albumId?.let { id ->
            val album = albumRepository.findById(id) ?: throw NotFoundException("Album with given id: $id was not found.")

            if (album.artistId != userId) {
                throw ForbiddenException("You don't have rights to album with id: $id.")
            }

            albumRepository.addSongs(id, listOf(songId))
        }

        return savedSong
    }

    fun getSongById(id: String): Song? =
        songRepository.findById(id)

    fun getAllSongs(pagination: Pagination): List<Song> =
        songRepository.findAll(pagination)

    fun deleteSong(id: String, userId: String) {
        val song = songRepository.findById(id) ?: throw NotFoundException("Song with given id: $id was not found.")

        if (userId != song.artistId) {
            throw ForbiddenException("You don't have rights to song with id: ${song.id}.")
        }

        songFileRepository.delete(song.file)
        song.imageFile?.let { imageFileRepository.delete(song.imageFile) }

        songRepository.deleteById(id)
    }
}
package pl.teamkiwi.domain.service

import org.joda.time.DateTime
import pl.teamkiwi.application.model.request.AlbumCreateRequest
import pl.teamkiwi.domain.`interface`.AlbumRepository
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.model.entity.Album
import pl.teamkiwi.domain.model.entity.ImageFile
import pl.teamkiwi.domain.model.exception.BadRequestException
import pl.teamkiwi.domain.model.exception.ForbiddenException
import pl.teamkiwi.domain.model.exception.NotFoundException
import pl.teamkiwi.domain.model.util.Pagination
import java.util.*

class AlbumService(
    private val albumRepository: AlbumRepository,
    private val songRepository: SongRepository
) {

    fun createAlbum(
        albumCreateRequest: AlbumCreateRequest,
        imageFile: ImageFile?,
        userId: String
    ): Album {
        val id = UUID.randomUUID().toString()

        val album = Album(
            id = id,
            artistId = userId,
            title = albumCreateRequest.title,
            imageFile = imageFile,
            uploadDate = DateTime.now()
        )

        return albumRepository.save(album)
    }

    fun getAlbumById(id: String): Album? =
        albumRepository.findById(id)

    fun getAllAlbums(pagination: Pagination): List<Album> =
        albumRepository.findAll(pagination)

    fun deleteAlbum(albumId: String, userId: String) {
        //check permission for album
        assertAlbumPermission(userId, albumId)

        albumRepository.deleteById(albumId)
    }

    fun addSongs(albumId: String, songIds: List<String>, userId: String) {
        //check permission for album
        assertAlbumPermission(userId, albumId)

        //check permission for all songs
        songIds.forEach { songId -> assertSongPermission(userId, songId) }

        //check if song is not already in any album
        songIds.forEach {
            val song = songRepository.findById(it)

            if (song?.albumId != null) {
                throw BadRequestException("Song with given id: ${song.id} is already in an album.")
            }
        }

        //check if songIds is distinct
        if (songIds.size != songIds.distinct().size) {
            throw BadRequestException("Song id list must be distinct.")
        }

        albumRepository.addSongs(albumId, songIds)
    }

    fun removeSongs(albumId: String, songIds: List<String>, userId: String) {
        //check permission for album
        assertAlbumPermission(userId, albumId)

        //check permission for all songs
        songIds.forEach { songId -> assertSongPermission(userId, songId) }

        //check if songIds is distinct
        if (songIds.size != songIds.distinct().size) {
            throw BadRequestException("Song id list must be distinct.")
        }

        albumRepository.removeSongs(songIds)
    }

    private fun assertAlbumPermission(userId: String, albumId: String) {
        val album = albumRepository.findById(albumId) ?: throw NotFoundException("Album with given id: $albumId was not found.")

        if (album.artistId != userId) {
            throw ForbiddenException("You don't have rights to album with id: $albumId.")
        }
    }

    private fun assertSongPermission(userId: String, songId: String) {
        val song = songRepository.findById(songId) ?: throw NotFoundException("Song with given id: $songId was not found.")

        if (song.artistId != userId) {
            throw ForbiddenException("You don't have rights to song with id: $songId.")
        }
    }
}
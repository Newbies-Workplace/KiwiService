package pl.teamkiwi.repository

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.converter.toAlbumDTO
import pl.teamkiwi.model.dto.AlbumDTO
import pl.teamkiwi.model.dto.create.AlbumCreateDTO
import pl.teamkiwi.repository.dao.AlbumDAO
import pl.teamkiwi.repository.dao.AlbumSongDAO
import pl.teamkiwi.repository.dao.SongDAO
import pl.teamkiwi.repository.table.AlbumSongs

class AlbumRepository {

    fun save(album: AlbumCreateDTO): AlbumDTO =
        transaction {
            AlbumDAO.new {
                title = album.title
                artistId = album.artistId
                imagePath = album.imagePath
                uploadDate = album.uploadDate
            }.toAlbumDTO()
        }

    fun findById(id: String): AlbumDTO? =
        transaction {
            AlbumDAO.findById(id)?.toAlbumDTO()
        }

    fun findAll(): List<AlbumDTO> =
        transaction {
            AlbumDAO.all()
                .map { it.toAlbumDTO() }
        }

    fun addSongs(albumId: String, songIds: List<String>) {
        val album = transaction {
            AlbumDAO[albumId]
        }

        val songs = transaction {
            SongDAO.forIds(songIds)
        }

        transaction {
            songs.map { song ->
                AlbumSongDAO.new {
                    this.album = album
                    this.song = song
                }
            }
        }
    }

    /**
     * We don't need to pass the albumId here, because song id is unique in [AlbumSongs]
     * and there can be only one song with specified id.
     */
    fun removeSongs(songIds: List<String>) {
        transaction {
            AlbumSongs.deleteWhere {
                AlbumSongs.song inList songIds
            }
        }
    }
}
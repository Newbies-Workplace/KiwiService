package pl.teamkiwi.infrastructure.repository

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.domain.`interface`.AlbumRepository
import pl.teamkiwi.domain.model.entity.Album
import pl.teamkiwi.infrastructure.repository.dao.AlbumDAO
import pl.teamkiwi.infrastructure.repository.dao.AlbumSongDAO
import pl.teamkiwi.infrastructure.repository.dao.SongDAO
import pl.teamkiwi.infrastructure.repository.table.AlbumSongs
import pl.teamkiwi.infrastructure.repository.table.Albums

class AlbumExposedRepository : AlbumRepository {

    override fun save(album: Album): Album =
        transaction {
            AlbumDAO.new(album.id) {
                title = album.title
                artistId = album.artistId
                imagePath = album.imagePath
                uploadDate = album.uploadDate
            }.toAlbum()
        }

    override fun findById(id: String): Album? =
        transaction {
            AlbumDAO.findById(id)?.toAlbum()
        }

    override fun findAll(): List<Album> =
        transaction {
            AlbumDAO.all()
                .map { it.toAlbum() }
        }

    override fun deleteById(id: String) {
        transaction {
            Albums.deleteWhere {
                Albums.id eq id
            }
        }
    }

    override fun addSongs(id: String, songIds: List<String>) {
        val album = transaction {
            AlbumDAO[id]
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

    override fun removeSongs(songIds: List<String>) {
        transaction {
            AlbumSongs.deleteWhere {
                AlbumSongs.song inList songIds
            }
        }
    }
}
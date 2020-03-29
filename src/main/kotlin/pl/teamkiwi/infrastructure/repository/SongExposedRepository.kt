package pl.teamkiwi.infrastructure.repository

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.model.entity.Song
import pl.teamkiwi.infrastructure.repository.dao.SongDAO
import pl.teamkiwi.infrastructure.repository.table.Songs

class SongExposedRepository : SongRepository {

    override fun save(song: Song): Song =
        transaction {
            SongDAO.new(song.id) {
                title = song.title
                path = song.path
                imagePath = song.imagePath
                artistId = song.artistId
                duration = song.duration
                uploadDate = song.uploadDate
            }.toSong()
        }

    override fun findById(id: String): Song? =
        transaction {
            SongDAO.findById(id)?.toSong()
        }

    override fun findAll(): List<Song> =
        transaction {
            SongDAO.all()
                .map { it.toSong() }
        }

    override fun deleteById(id: String) {
        transaction {
            Songs.deleteWhere { Songs.id eq id }
        }
    }
}
package pl.teamkiwi.repository

import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.converter.toSongDTO
import pl.teamkiwi.model.dto.SongDTO
import pl.teamkiwi.model.dto.create.SongCreateDTO
import pl.teamkiwi.repository.dao.SongDAO
import java.util.*

class SongRepository {

    fun save(song: SongCreateDTO): SongDTO =
        transaction {
            SongDAO.new {
                title = song.title
                path = song.path
                imagePath = song.imagePath
                artistId = song.artistId
                duration = song.duration
                uploadDate = song.uploadDate
            }.toSongDTO()
        }

    fun findById(id: UUID): SongDTO? =
        transaction {
            SongDAO.findById(id)?.toSongDTO()
        }

    fun findAll(): List<SongDTO> =
        transaction {
            SongDAO.all()
                .map { it.toSongDTO() }
        }
}
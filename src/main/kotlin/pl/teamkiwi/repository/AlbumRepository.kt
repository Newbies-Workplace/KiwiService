package pl.teamkiwi.repository

import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.converter.toAlbumDTO
import pl.teamkiwi.model.dto.AlbumDTO
import pl.teamkiwi.model.dto.create.AlbumCreateDTO
import pl.teamkiwi.repository.dao.AlbumDAO

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
}
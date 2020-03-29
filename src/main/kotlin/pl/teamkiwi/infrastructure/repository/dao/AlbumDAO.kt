package pl.teamkiwi.infrastructure.repository.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.domain.model.entity.Album
import pl.teamkiwi.infrastructure.repository.table.Albums
import pl.teamkiwi.infrastructure.repository.table.StringIdEntity
import pl.teamkiwi.infrastructure.repository.table.StringIdEntityClass

class AlbumDAO(id: EntityID<String>) : StringIdEntity(id) {
    companion object : StringIdEntityClass<AlbumDAO>(Albums)

    var title by Albums.title
    var imagePath by Albums.imagePath
    var artistId by Albums.artistId
    var uploadDate by Albums.uploadDate

    fun toAlbum() =
        Album(
            id = id.value,
            title = title,
            artistId = artistId,
            imagePath = imagePath,
            uploadDate = uploadDate
        )
}
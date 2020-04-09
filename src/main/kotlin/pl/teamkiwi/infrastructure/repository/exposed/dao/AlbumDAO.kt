package pl.teamkiwi.infrastructure.repository.exposed.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.domain.model.entity.Album
import pl.teamkiwi.domain.model.entity.ImageFile
import pl.teamkiwi.infrastructure.repository.exposed.table.Albums
import pl.teamkiwi.infrastructure.repository.exposed.table.StringIdEntity
import pl.teamkiwi.infrastructure.repository.exposed.table.StringIdEntityClass

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
            imageFile = imagePath?.let { ImageFile(it) },
            uploadDate = uploadDate
        )
}
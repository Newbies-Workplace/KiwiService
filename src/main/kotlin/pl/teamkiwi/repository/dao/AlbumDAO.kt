package pl.teamkiwi.repository.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.repository.table.Albums
import pl.teamkiwi.repository.table.StringIdEntity
import pl.teamkiwi.repository.table.StringIdEntityClass

class AlbumDAO(id: EntityID<String>) : StringIdEntity(id) {
    companion object : StringIdEntityClass<AlbumDAO>(Albums)

    var title by Albums.title
    var imagePath by Albums.imagePath
    var artistId by Albums.artistId
    var uploadDate by Albums.uploadDate
}
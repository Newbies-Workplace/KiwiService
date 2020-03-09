package pl.teamkiwi.repository.dao

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import pl.teamkiwi.repository.table.Songs
import java.util.*

class SongDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SongDAO>(Songs)

    var title by Songs.title
    var path by Songs.path
    var imagePath by Songs.imagePath
    var artistId by Songs.artistId
    var duration by Songs.duration
    var uploadDate by Songs.uploadDate
}
package pl.teamkiwi.repository.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.repository.table.AlbumSongs
import pl.teamkiwi.repository.table.Songs
import pl.teamkiwi.repository.table.StringIdEntity
import pl.teamkiwi.repository.table.StringIdEntityClass

class SongDAO(id: EntityID<String>) : StringIdEntity(id) {
    companion object : StringIdEntityClass<SongDAO>(Songs)

    var title by Songs.title
    var path by Songs.path
    var imagePath by Songs.imagePath
    var artistId by Songs.artistId
    var album by AlbumDAO via AlbumSongs
    var duration by Songs.duration
    var uploadDate by Songs.uploadDate
}
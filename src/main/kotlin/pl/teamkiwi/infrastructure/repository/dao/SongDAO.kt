package pl.teamkiwi.infrastructure.repository.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.domain.model.entity.Song
import pl.teamkiwi.infrastructure.repository.table.AlbumSongs
import pl.teamkiwi.infrastructure.repository.table.Songs
import pl.teamkiwi.infrastructure.repository.table.StringIdEntity
import pl.teamkiwi.infrastructure.repository.table.StringIdEntityClass

class SongDAO(id: EntityID<String>) : StringIdEntity(id) {
    companion object : StringIdEntityClass<SongDAO>(Songs)

    var title by Songs.title
    var path by Songs.path
    var imagePath by Songs.imagePath
    var artistId by Songs.artistId
    var album by AlbumDAO via AlbumSongs
    var duration by Songs.duration
    var uploadDate by Songs.uploadDate

    fun toSong() =
        Song(
            id = id.value,
            title = title,
            path = path,
            imagePath = imagePath,
            artistId = artistId,
            albumId = album.firstOrNull()?.id?.value,
            duration = duration,
            uploadDate = uploadDate
        )
}
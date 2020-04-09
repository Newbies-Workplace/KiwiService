package pl.teamkiwi.infrastructure.repository.exposed.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.domain.model.entity.ImageFile
import pl.teamkiwi.domain.model.entity.Song
import pl.teamkiwi.domain.model.entity.SongFile
import pl.teamkiwi.infrastructure.repository.exposed.table.AlbumSongs
import pl.teamkiwi.infrastructure.repository.exposed.table.Songs
import pl.teamkiwi.infrastructure.repository.exposed.table.StringIdEntity
import pl.teamkiwi.infrastructure.repository.exposed.table.StringIdEntityClass

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
            file = SongFile(path),
            imageFile = imagePath?.let { ImageFile(it) },
            artistId = artistId,
            albumId = album.firstOrNull()?.id?.value,
            duration = duration,
            uploadDate = uploadDate
        )
}
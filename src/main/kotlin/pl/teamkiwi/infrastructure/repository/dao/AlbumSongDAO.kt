package pl.teamkiwi.infrastructure.repository.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.infrastructure.repository.table.AlbumSongs
import pl.teamkiwi.infrastructure.repository.table.StringIdEntity
import pl.teamkiwi.infrastructure.repository.table.StringIdEntityClass

class AlbumSongDAO(id: EntityID<String>): StringIdEntity(id) {
    companion object : StringIdEntityClass<AlbumSongDAO>(AlbumSongs)

    var album by AlbumDAO referencedOn AlbumSongs.album
    var song by SongDAO referencedOn AlbumSongs.song
}
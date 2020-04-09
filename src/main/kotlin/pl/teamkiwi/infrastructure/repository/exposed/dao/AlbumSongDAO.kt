package pl.teamkiwi.infrastructure.repository.exposed.dao

import org.jetbrains.exposed.dao.EntityID
import pl.teamkiwi.infrastructure.repository.exposed.table.AlbumSongs
import pl.teamkiwi.infrastructure.repository.exposed.table.StringIdEntity
import pl.teamkiwi.infrastructure.repository.exposed.table.StringIdEntityClass

class AlbumSongDAO(id: EntityID<String>): StringIdEntity(id) {
    companion object : StringIdEntityClass<AlbumSongDAO>(AlbumSongs)

    var album by AlbumDAO referencedOn AlbumSongs.album
    var song by SongDAO referencedOn AlbumSongs.song
}
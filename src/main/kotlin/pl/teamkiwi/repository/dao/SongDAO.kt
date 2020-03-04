package pl.teamkiwi.repository.dao

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import pl.teamkiwi.repository.table.Songs
import pl.teamkiwi.repository.table.Users
import java.util.*

class SongDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDAO>(Users)

    val title by Songs.title
    val imagePath by Songs.imagePath
    val artist by Songs.artist
    val artistId by Songs.artistId
    val composeDate by Songs.composeDate
    val uploadDate by Songs.uploadDate
    val path by Songs.path
    val albumId by Songs.albumId
    val duration by Songs.duration
}
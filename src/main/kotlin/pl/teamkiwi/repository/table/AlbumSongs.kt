package pl.teamkiwi.repository.table

import org.jetbrains.exposed.sql.ReferenceOption

object AlbumSongs: StringIdTable() {
    val album = reference("album", Albums, ReferenceOption.CASCADE)
    val song = reference("song", Songs, ReferenceOption.CASCADE).uniqueIndex()
}
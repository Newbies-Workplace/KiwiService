package pl.teamkiwi.repository.table

object AlbumSongs: StringIdTable() {
    val album = reference("album", Albums)
    val song = reference("song", Songs).uniqueIndex()
}
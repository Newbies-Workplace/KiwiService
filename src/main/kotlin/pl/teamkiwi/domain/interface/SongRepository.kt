package pl.teamkiwi.domain.`interface`

import pl.teamkiwi.domain.model.entity.Song

interface SongRepository {

    fun save(song: Song): Song

    fun findById(id: String): Song?

    fun findIdsByAlbumId(albumId: String): List<String>

    fun findAll(): List<Song>

    fun deleteById(id: String)
}
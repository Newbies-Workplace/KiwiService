package pl.teamkiwi.domain.`interface`

import pl.teamkiwi.domain.model.entity.Song
import pl.teamkiwi.domain.model.util.Pagination

interface SongRepository {

    fun save(song: Song): Song

    fun findById(id: String): Song?

    fun findIdsByAlbumId(albumId: String): List<String>

    fun findAll(pagination: Pagination): List<Song>

    fun deleteById(id: String)
}
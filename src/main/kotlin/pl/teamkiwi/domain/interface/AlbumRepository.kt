package pl.teamkiwi.domain.`interface`

import pl.teamkiwi.domain.model.entity.Album
import pl.teamkiwi.domain.model.util.Pagination
import pl.teamkiwi.infrastructure.repository.exposed.table.AlbumSongs

interface AlbumRepository {

    fun save(album: Album): Album

    fun findById(id: String): Album?

    fun findAll(pagination: Pagination): List<Album>

    fun deleteById(id: String)

    fun addSongs(id: String, songIds: List<String>)

    /**
     * We don't need to pass the albumId here, because song id is unique in [AlbumSongs]
     * and there can be only one song with specified id.
     */
    fun removeSongs(songIds: List<String>)
}
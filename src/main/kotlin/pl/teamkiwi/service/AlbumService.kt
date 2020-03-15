package pl.teamkiwi.service

import pl.teamkiwi.model.dto.AlbumDTO
import pl.teamkiwi.model.dto.create.AlbumCreateDTO
import pl.teamkiwi.repository.AlbumRepository

class AlbumService(
    private val albumRepository: AlbumRepository
) {

    fun save(albumCreateDTO: AlbumCreateDTO): AlbumDTO =
        albumRepository.save(albumCreateDTO)

    fun findById(id: String): AlbumDTO? =
        albumRepository.findById(id)

    fun findAll(): List<AlbumDTO> =
        albumRepository.findAll()

    fun addSongs(id: String, songIds: List<String>) =
        albumRepository.addSongs(id, songIds)

    fun removeSongs(songIds: List<String>) =
        albumRepository.removeSongs(songIds)
}
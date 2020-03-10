package pl.teamkiwi.service

import pl.teamkiwi.model.dto.SongDTO
import pl.teamkiwi.model.dto.create.SongCreateDTO
import pl.teamkiwi.repository.SongRepository

class SongService(
    private val songRepository: SongRepository
) {

    fun save(song: SongCreateDTO): SongDTO =
        songRepository.save(song)

    fun findById(id: String): SongDTO? =
        songRepository.findById(id)

    fun findAll(): List<SongDTO> =
        songRepository.findAll()

    fun deleteById(id: String) =
        songRepository.delete(id)
}
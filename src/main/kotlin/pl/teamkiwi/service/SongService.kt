package pl.teamkiwi.service

import pl.teamkiwi.model.dto.SongDTO
import pl.teamkiwi.model.dto.create.SongCreateDTO
import pl.teamkiwi.repository.SongRepository
import java.util.*

class SongService(
    private val songRepository: SongRepository
) {

    fun save(song: SongCreateDTO): SongDTO =
        songRepository.save(song)

    fun findById(id: UUID): SongDTO? =
        songRepository.findById(id)

    fun findAll(): List<SongDTO> =
        songRepository.findAll()
}
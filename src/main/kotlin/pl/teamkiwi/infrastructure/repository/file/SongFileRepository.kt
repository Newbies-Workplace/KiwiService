package pl.teamkiwi.infrastructure.repository.file

import pl.teamkiwi.domain.model.entity.SongFile

class SongFileRepository(
    configuration: Configuration
) : FileRepository<SongFile>(configuration) {

    override fun domainFileForName(name: String): SongFile {
        assertValidFileName(name)

        return SongFile(name)
    }
}
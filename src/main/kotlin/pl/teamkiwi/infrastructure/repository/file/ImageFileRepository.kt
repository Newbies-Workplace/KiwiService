package pl.teamkiwi.infrastructure.repository.file

import pl.teamkiwi.domain.model.entity.ImageFile

class ImageFileRepository(
    configuration: Configuration
) : FileRepository<ImageFile>(configuration) {

    override fun domainFileForName(name: String): ImageFile {
        assertValidFileName(name)

        return ImageFile(name)
    }
}
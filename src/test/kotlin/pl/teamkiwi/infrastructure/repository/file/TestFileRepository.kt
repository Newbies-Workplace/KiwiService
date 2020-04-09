package pl.teamkiwi.infrastructure.repository.file

import pl.teamkiwi.domain.model.entity.DomainFile

class TestFileRepository(
    configuration: Configuration
) : FileRepository<TestFile>(configuration) {

    override fun domainFileForName(name: String): TestFile {
        assertValidFileName(name)

        return TestFile(name)
    }
}

class TestFile(name: String): DomainFile(name)
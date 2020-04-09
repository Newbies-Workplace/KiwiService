package pl.teamkiwi.infrastructure.repository.file

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import pl.teamkiwi.domain.model.exception.NotFoundException
import java.nio.file.Files
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class FileRepositoryTest {

    private val uploadDir = "file"

    private val config = FileRepository.Configuration(
        uploadDir, listOf("ext")
    )

    private val testFileRepository = TestFileRepository(config)

    @Nested
    inner class Initialize {

        @Test
        fun `should create upload paths when constructed`() {
            //given
            val uploadPath = Paths.get(uploadDir)

            mockkStatic(Paths::class)
            mockkStatic(Files::class)

            every { Paths.get(uploadDir) } returns uploadPath

            //when
            TestFileRepository(config)

            //then
            verify { Files.createDirectories(uploadPath) }
        }
    }

    @Nested
    inner class GetFile {

        @Test
        fun `should throw NotFoundException when there is no file with given name`() {
            //given
            val testFile = TestFile("name.ext")

            mockkStatic(Files::class)

            every { Files.notExists(any()) } returns true

            //when
            assertThrows<NotFoundException> {
                testFileRepository.get(testFile)
            }
        }
    }

    @Nested
    inner class Delete {

        @Test
        fun `should call File delete() given testFile`() {
            //given
            val fileName = "filename.ext"
            val testFile = TestFile(fileName)
            val filePath = Paths.get("${config.uploadPath}/${fileName}")

            mockkStatic(Paths::class)
            mockkStatic(Files::class)

            every { Paths.get("${config.uploadPath}/${fileName}") } returns filePath

            //when
            testFileRepository.delete(testFile)

            //then
            verify { Files.deleteIfExists(filePath) }
        }
    }
}
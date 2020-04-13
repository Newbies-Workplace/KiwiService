package pl.teamkiwi.infrastructure.repository.file

import pl.teamkiwi.domain.model.entity.DomainFile
import pl.teamkiwi.domain.model.exception.*
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

abstract class FileRepository<T: DomainFile>(
    private val configuration: Configuration
) {

    init {
        initializeUploadDirectory()
    }

    fun delete(domainFile: T) {
        runCatching {
            val path = Paths.get("${configuration.uploadPath}/${domainFile.name}")

            Files.deleteIfExists(path)
        }.getOrElse {
            throw FileDeleteException("File deleting failed with exception.", it)
        }
    }

    fun get(domainFile: T): File {
        val path = configuration.uploadPath + "/" + domainFile.name

        if (Files.notExists(Paths.get(path))) {
            throw NotFoundException("File with given name not found.")
        }

        return File(path)
    }

    fun save(inputStream: InputStream, extension: String): T {
        val fileName = "${UUID.randomUUID()}.$extension"

        runCatching {
            val file = File(configuration.uploadPath, fileName)

            file.outputStream()
                .buffered()
                .use { inputStream.copyTo(it) }

            return domainFileForName(fileName)
        }.getOrElse {
            throw FileSaveException("File could not be saved.", it)
        }
    }

    abstract fun domainFileForName(name: String): T

    protected fun assertValidFileName(fileName: String) {
        if (fileName.contains("..")) {
            throw BadRequestException("Filename contains invalid characters.")
        }

        val extension = fileName.getExtension()

        if (!configuration.allowedExtensions.contains(extension)) {
            throw UnsupportedExtensionException("Extension .$extension is not supported.")
        }
    }

    private fun initializeUploadDirectory() {
        val uploadPath = Paths.get(configuration.uploadPath)

        try {
            Files.createDirectories(uploadPath)
        } catch (e: FileAlreadyExistsException) {}
    }

    fun String?.getExtension() = this?.substringAfterLast('.', "")

    data class Configuration(
        val uploadPath: String,
        val allowedExtensions: List<String>
    )
}
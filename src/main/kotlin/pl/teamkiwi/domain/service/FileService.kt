package pl.teamkiwi.domain.service

import  io.ktor.http.content.PartData
import io.ktor.http.content.streamProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import pl.teamkiwi.domain.model.exception.FileSaveException
import pl.teamkiwi.domain.model.exception.UnsupportedExtensionException
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*

//todo refactor (return filename instead of path)
class FileService(
    private val config: Configuration
) {

    suspend fun saveSong(fileItem: PartData.FileItem): String {
        assertSongExtension(fileItem.originalFileName.getExtension())

        return saveFile(fileItem, config.songPath)
    }

    suspend fun saveImage(fileItem: PartData.FileItem): String {
        assertImageExtension(fileItem.originalFileName.getExtension())

        return saveFile(fileItem, config.imagePath)
    }

    private suspend fun saveFile(fileItem: PartData.FileItem, path: String): String {
        val ext = fileItem.originalFileName.getExtension()
        val file = File(path, "${UUID.randomUUID()}.$ext")

        runCatching {
            fileItem.streamProvider()
                .use { input ->
                    file.outputStream()
                        .buffered()
                        .use { output ->
                            input.copyToSuspend(output)
                        }
                }

            //todo return fileName
            return file.absolutePath
        }.getOrElse {
            throw FileSaveException()
        }
    }

    fun deleteFile(path: String) {
        runCatching {
            File(path).delete()
        }
    }

    private fun assertSongExtension(extension: String?) {
        if (!config.songExtensions.contains(extension)) throw UnsupportedExtensionException()
    }

    private fun assertImageExtension(extension: String?) {
        if (!config.imageExtensions.contains(extension)) throw UnsupportedExtensionException()
    }

    private fun String?.getExtension() = this?.substringAfterLast('.', "")

    //copied from https://ktor.io/servers/uploads.html
    private suspend fun InputStream.copyToSuspend(
        out: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
        yieldSize: Int = 4 * 1024 * 1024,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Long {
        return withContext(dispatcher) {
            val buffer = ByteArray(bufferSize)
            var bytesCopied = 0L
            var bytesAfterYield = 0L
            while (true) {
                val bytes = read(buffer).takeIf { it >= 0 } ?: break
                out.write(buffer, 0, bytes)
                if (bytesAfterYield >= yieldSize) {
                    yield()
                    bytesAfterYield %= yieldSize
                }
                bytesCopied += bytes
                bytesAfterYield += bytes
            }
            return@withContext bytesCopied
        }
    }

    data class Configuration(
        val songPath: String,
        val imagePath: String,
        val songExtensions: List<String>,
        val imageExtensions: List<String>
    )
}
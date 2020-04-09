package pl.teamkiwi.application.controller

import io.ktor.application.ApplicationCall
import io.ktor.http.content.LocalFileContent
import io.ktor.response.respond
import pl.teamkiwi.infrastructure.repository.file.ImageFileRepository
import pl.teamkiwi.infrastructure.repository.file.SongFileRepository

class FileController(
    private val imageFileRepository: ImageFileRepository,
    private val songFileRepository: SongFileRepository
) {

    suspend fun getImage(call: ApplicationCall, imageName: String) {
        val imageFile = imageFileRepository.domainFileForName(imageName)
        val image = imageFileRepository.get(imageFile)

        val response = LocalFileContent(image)

        call.respond(response)
    }

    suspend fun getSong(call: ApplicationCall, songName: String) {
        val songFile = songFileRepository.domainFileForName(songName)
        val song = songFileRepository.get(songFile)

        val response = LocalFileContent(song)

        call.respond(response)
    }
}
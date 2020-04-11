package pl.teamkiwi.application.converter

import pl.teamkiwi.application.model.response.AlbumResponse
import pl.teamkiwi.application.util.DownloadPathProvider
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.model.entity.Album

class AlbumConverter(
    private val pathProvider: DownloadPathProvider,
    private val songRepository: SongRepository
) {

    fun Album.toAlbumResponse() =
        AlbumResponse(
            id = id,
            title = title,
            artistId = artistId,
            imagePath = imageFile?.let { pathProvider.imagePath(it.name) },
            uploadDate = uploadDate.millis,
            songs = songRepository.findIdsByAlbumId(id)
        )
}
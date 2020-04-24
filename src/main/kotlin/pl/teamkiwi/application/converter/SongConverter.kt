package pl.teamkiwi.application.converter

import pl.teamkiwi.application.model.event.payload.SongPayload
import pl.teamkiwi.application.model.response.SongResponse
import pl.teamkiwi.application.util.DownloadPathProvider
import pl.teamkiwi.domain.model.entity.Song

class SongConverter(
    private val pathProvider: DownloadPathProvider
) {

    fun Song.toSongResponse() =
        SongResponse(
            id = id,
            title = title,
            imagePath = imageFile?.let { pathProvider.imagePath(it.name) },
            artistId = artistId,
            uploadDate = uploadDate.millis,
            albumId = albumId,
            duration = duration,
            path = pathProvider.songPath(file.name)
        )

    fun Song.toSongPayload() =
        SongPayload(
            id = id,
            title = title,
            imagePath = imageFile?.let { pathProvider.imagePath(it.name) },
            path = pathProvider.songPath(file.name),
            artistId = artistId
        )
}
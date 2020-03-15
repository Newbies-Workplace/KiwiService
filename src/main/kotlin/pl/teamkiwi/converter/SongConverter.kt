package pl.teamkiwi.converter

import org.joda.time.DateTime
import pl.teamkiwi.model.dto.SongDTO
import pl.teamkiwi.model.dto.create.SongCreateDTO
import pl.teamkiwi.model.request.SongCreateRequest
import pl.teamkiwi.repository.dao.SongDAO

fun SongDAO.toSongDTO() =
    SongDTO(
        id = id.value,
        title = title,
        path = path,
        imagePath = imagePath,
        artistId = artistId,
        albumId = album.firstOrNull()?.id?.value,
        duration = duration,
        uploadDate = uploadDate.toDate()
    )

fun SongCreateRequest.toSongCreateDTO(
    path: String,
    imagePath: String?,
    artistId: String,
    duration: Long
) =
    SongCreateDTO(
        title = title,
        path = path,
        imagePath = imagePath,
        artistId = artistId,
        duration = duration,
        uploadDate = DateTime.now()
    )
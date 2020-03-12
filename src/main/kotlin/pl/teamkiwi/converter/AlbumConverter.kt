package pl.teamkiwi.converter

import org.joda.time.DateTime
import pl.teamkiwi.model.dto.AlbumDTO
import pl.teamkiwi.model.dto.create.AlbumCreateDTO
import pl.teamkiwi.model.request.AlbumCreateRequest
import pl.teamkiwi.repository.dao.AlbumDAO

fun AlbumDAO.toAlbumDTO() =
    AlbumDTO(
        id = id.value,
        title = title,
        artistId = artistId,
        imagePath = imagePath,
        uploadDate = uploadDate.toDate()
    )

fun AlbumCreateRequest.toAlbumCreateDTO(
    artistId: String,
    imagePath: String?
) = AlbumCreateDTO(
    title = title,
    artistId = artistId,
    imagePath = imagePath,
    uploadDate = DateTime.now()
)
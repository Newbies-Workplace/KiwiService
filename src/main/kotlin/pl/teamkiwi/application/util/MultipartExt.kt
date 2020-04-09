package pl.teamkiwi.application.util

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import pl.teamkiwi.domain.model.entity.DomainFile
import pl.teamkiwi.domain.model.exception.BadRequestException
import pl.teamkiwi.infrastructure.repository.file.FileRepository

/**
 * Creates a map with key equals to part name
 *
 * @throws NullPointerException when there is a part without name.
 */
suspend fun MultiPartData.toMap(): MutableMap<String, PartData> {
    val map = mutableMapOf<String, PartData>()

    forEachPart {
        val partName = it.name ?: throw NullPointerException("part name is null")

        require(partName.isNotBlank()) { "part name is blank" }

        map[partName] = it
    }

    return map
}

const val SONG_PART_KEY = "song"
const val IMAGE_PART_KEY = "image"
const val REQUEST_PART_KEY = "request"

fun Map<String, PartData>.getSongOrNull(): PartData.FileItem? =
    get(SONG_PART_KEY) as? PartData.FileItem

fun Map<String, PartData>.getImageOrNull(): PartData.FileItem? =
    get(IMAGE_PART_KEY) as? PartData.FileItem

inline fun <reified T : Any> Map<String, PartData>.getRequestOrNull(): T? {
    val item = get(REQUEST_PART_KEY) as? PartData.FormItem

    return item?.let { deserializeOrNull<T>(it.value) }
}

fun MutableMap<String, PartData>.dispose() {
    forEach {
        it.value.dispose()
    }
    clear()
}

fun <T: DomainFile> FileRepository<T>.save(item: PartData.FileItem): T {
    val inputStream = item.streamProvider()
    val extension = item.originalFileName.getExtension() ?: throw BadRequestException()

    return save(inputStream, extension)
}
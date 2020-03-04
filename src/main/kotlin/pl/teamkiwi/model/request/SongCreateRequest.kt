package pl.teamkiwi.model.request

import org.valiktor.validate
import java.util.*

data class SongCreateRequest(
    val title: String,
    val artist: String?,
    val composeDate: Date?,
    val albumId: String?
) {

    init {
        validate(this) {
            //todo SongCreateRequest validate
        }
    }
}

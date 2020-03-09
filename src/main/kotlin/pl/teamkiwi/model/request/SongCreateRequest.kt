package pl.teamkiwi.model.request

import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import pl.teamkiwi.repository.constants.TITLE_MAX_LENGTH

data class SongCreateRequest(
    val title: String
) {

    init {
        validate(this) {
            validate(SongCreateRequest::title)
                .isNotBlank()
                .hasSize(max = TITLE_MAX_LENGTH)
        }
    }
}

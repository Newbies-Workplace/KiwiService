package pl.teamkiwi.model.request

import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import pl.teamkiwi.repository.constants.TITLE_MAX_LENGTH

data class AlbumCreateRequest(
    val title: String
) {
    init {

        validate(this) {
            validate(AlbumCreateRequest::title)
                .isNotBlank()
                .hasSize(max = TITLE_MAX_LENGTH)
        }
    }
}
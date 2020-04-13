package pl.teamkiwi.domain.model.util

import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate
import pl.teamkiwi.domain.model.exception.BadPaginationException

data class Pagination(
    val limit: Int,
    val offset: Int
) {

    init {
        runCatching {
            validate(this) {
                validate(Pagination::limit)
                    .isLessThanOrEqualTo(MAX_LIMIT_SIZE)
                    .isGreaterThanOrEqualTo(MIN_LIMIT_SIZE)
                validate(Pagination::offset)
                    .isGreaterThanOrEqualTo(0)
            }
        }.getOrElse {
            throw BadPaginationException("Bad pagination parameters.", it)
        }
    }
}

val DEFAULT_PAGINATION =
    Pagination(
        limit = 30,
        offset = 0
    )

const val MAX_LIMIT_SIZE = 100
const val MIN_LIMIT_SIZE = 10
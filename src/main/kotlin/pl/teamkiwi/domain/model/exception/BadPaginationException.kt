package pl.teamkiwi.domain.model.exception

class BadPaginationException(
    message: String,
    cause: Throwable
) : NotFoundException(message, cause)
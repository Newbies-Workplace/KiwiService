package pl.teamkiwi.domain.model.exception

class FileDeleteException(
    message: String,
    cause: Throwable
) : Exception(message, cause)
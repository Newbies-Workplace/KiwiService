package pl.teamkiwi.domain.model.exception

class FileSaveException(
    message: String,
    cause: Throwable
) : Exception(message, cause)
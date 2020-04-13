package pl.teamkiwi.domain.model.exception

open class BadRequestException : Exception {
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(message: String) : super(message)
}
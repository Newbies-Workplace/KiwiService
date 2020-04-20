package pl.teamkiwi.domain.model.exception

class UnauthorizedException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
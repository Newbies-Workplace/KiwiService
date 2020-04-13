package pl.teamkiwi.domain.model.exception

class AccountAlreadyExistsException(
    message: String
) : ConflictException(message)
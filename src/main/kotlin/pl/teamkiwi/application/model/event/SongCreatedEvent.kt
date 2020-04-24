package pl.teamkiwi.application.model.event

import pl.teamkiwi.application.model.event.payload.SongPayload

data class SongCreatedEvent(
    val song: SongPayload
)
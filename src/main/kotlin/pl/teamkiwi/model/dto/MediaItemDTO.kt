package pl.teamkiwi.model.dto

import java.util.*

open class MediaItemDTO(
    open val id: String,
    open val title: String,
    open val artist: String?,
    open val artistId: String,
    open val imagePath: String?,
    open val composeDate: Date?
)
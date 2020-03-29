package pl.teamkiwi.domain.model.entity

abstract class MediaItem(
    open val id: String,
    open val title: String,
    open val artistId: String,
    open val imagePath: String?
)
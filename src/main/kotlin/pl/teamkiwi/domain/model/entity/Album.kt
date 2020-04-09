package pl.teamkiwi.domain.model.entity

import org.joda.time.DateTime

data class Album(
    val id: String,
    val title: String,
    val artistId: String,
    val imageFile: ImageFile?,
    val uploadDate: DateTime
)
package pl.teamkiwi.domain.model.entity

data class SongFile(
    override val name: String
) : DomainFile(name)
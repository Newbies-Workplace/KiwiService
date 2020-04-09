package pl.teamkiwi.domain.model.entity

data class ImageFile(
    override val name: String
) : DomainFile(name)
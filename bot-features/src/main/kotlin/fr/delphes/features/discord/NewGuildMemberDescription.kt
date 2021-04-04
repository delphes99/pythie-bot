package fr.delphes.features.discord

import fr.delphes.feature.NonEditableFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("discord-new-guild-member")
class NewGuildMemberDescription : NonEditableFeatureDescription {
    override val editable = false
    override val id: String = UUID.randomUUID().toString()
}
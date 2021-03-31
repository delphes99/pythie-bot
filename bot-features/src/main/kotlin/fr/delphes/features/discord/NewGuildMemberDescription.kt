package fr.delphes.features.discord

import fr.delphes.feature.FeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("discord-new-guild-member")
class NewGuildMemberDescription : FeatureDescription {
    override val id: String = UUID.randomUUID().toString()
}
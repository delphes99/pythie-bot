package fr.delphes.features.twitch.newSub

import fr.delphes.features.twitch.TwitchFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("twitch-new-sub")
class NewSubDescription(
    override val channel: String
) : TwitchFeatureDescription {
    override val id: String = UUID.randomUUID().toString()
}
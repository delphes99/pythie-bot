package fr.delphes.features.twitch.newFollow

import fr.delphes.features.twitch.TwitchFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("twitch-new-follow")
class NewFollowDescription(
    override val channel: String
) : TwitchFeatureDescription {
    override val id: String = UUID.randomUUID().toString()
}
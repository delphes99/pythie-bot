package fr.delphes.features.twitch.commandList

import fr.delphes.features.twitch.TwitchFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("twitch-command-list")
class CommandListDescription(
    override val channel: String,
    val triggerMessage: String
) : TwitchFeatureDescription {
    override val id: String = UUID.randomUUID().toString()
}
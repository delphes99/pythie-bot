package fr.delphes.features.twitch.gameDescription

import fr.delphes.features.twitch.TwitchFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("twitch-game-description")
class GameDescriptionDescription(
    override val channel: String
) : TwitchFeatureDescription {
    override val editable = false
    override val id: String = UUID.randomUUID().toString()
}
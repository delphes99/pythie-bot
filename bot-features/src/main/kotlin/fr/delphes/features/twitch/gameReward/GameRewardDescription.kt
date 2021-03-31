package fr.delphes.features.twitch.gameReward

import fr.delphes.features.twitch.TwitchFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("twitch-game-reward")
class GameRewardDescription(
    override val channel: String
) : TwitchFeatureDescription {
    override val id: String = UUID.randomUUID().toString()
}
package fr.delphes.connector.twitch.reward

import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.Serializable

@Serializable
data class RewardId(
    val channel: TwitchChannel,
    val title: RewardTitle,
)
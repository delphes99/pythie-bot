package fr.delphes.connector.twitch.reward

import fr.delphes.twitch.TwitchChannel

data class ChannelRewardId(
    val title: String,
    val channel: TwitchChannel,
)

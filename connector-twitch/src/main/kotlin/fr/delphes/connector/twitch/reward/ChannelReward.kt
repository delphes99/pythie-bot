package fr.delphes.connector.twitch.reward

import fr.delphes.twitch.api.reward.RewardConfiguration

data class ChannelReward(
    val id: ChannelRewardId,
    val configuration: RewardConfiguration,
)
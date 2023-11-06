package fr.delphes.twitch.api.channelPointsCustomRewardRedemption

import fr.delphes.twitch.api.reward.TwitchRewardId

data class RewardRedemption(
    val rewardId: TwitchRewardId,
    val redemptionId: String,
)
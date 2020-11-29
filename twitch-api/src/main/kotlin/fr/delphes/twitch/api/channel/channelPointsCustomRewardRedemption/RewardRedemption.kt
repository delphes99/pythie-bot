package fr.delphes.twitch.api.channel.channelPointsCustomRewardRedemption

import fr.delphes.twitch.api.user.User

data class RewardRedemption(
    val reward: Reward,
    val user: User,
    val cost: RewardCost
)
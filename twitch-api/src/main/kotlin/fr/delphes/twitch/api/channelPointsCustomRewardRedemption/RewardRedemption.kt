package fr.delphes.twitch.api.channelPointsCustomRewardRedemption

import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.user.User

data class RewardRedemption(
    val reward: Reward,
    val user: User,
    val cost: RewardCost,
    internal val id: String
)
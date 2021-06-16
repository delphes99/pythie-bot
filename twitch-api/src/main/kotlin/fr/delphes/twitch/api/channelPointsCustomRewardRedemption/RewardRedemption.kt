package fr.delphes.twitch.api.channelPointsCustomRewardRedemption

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.RewardId
import fr.delphes.twitch.api.user.User

data class RewardRedemption(
    val channel: TwitchChannel,
    val reward: RewardId,
    val user: User,
    val cost: RewardCost,
    internal val id: String
)
package fr.delphes.twitch.api.channelPointsCustomRewardRedemption

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.TwitchRewardId
import fr.delphes.twitch.api.user.UserName

data class RewardRedemption(
    val channel: TwitchChannel,
    val reward: TwitchRewardId,
    val user: UserName,
    val cost: RewardCost,
    internal val id: String,
)
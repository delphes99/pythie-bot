package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardCost
import fr.delphes.twitch.api.reward.RewardId
import fr.delphes.twitch.api.user.UserName

data class RewardRedemption(
    override val channel: TwitchChannel,
    val reward: RewardId,
    val user: UserName,
    val cost: RewardCost
) : TwitchIncomingEvent {
    constructor(
        channel: TwitchChannel,
        reward: RewardId,
        user: String,
        cost: RewardCost
    ) : this(
        channel,
        reward,
        UserName(user),
        cost
    )
}
package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardCost
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption
import fr.delphes.twitch.api.reward.RewardId
import fr.delphes.twitch.api.user.User

data class RewardRedemption(
    override val channel: TwitchChannel,
    val reward: RewardId,
    val user: User,
    val cost: RewardCost
) : TwitchIncomingEvent {
    constructor(
        channel: TwitchChannel,
        event: RewardRedemption
    ) : this(
        channel,
        event.reward,
        event.user,
        event.cost
    )

    constructor(
        channel: TwitchChannel,
        reward: RewardId,
        user: String,
        cost: RewardCost
    ) : this(
        channel,
        reward,
        User(user),
        cost
    )
}
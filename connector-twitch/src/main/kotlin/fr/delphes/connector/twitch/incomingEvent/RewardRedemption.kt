package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.annotation.incomingEvent.RegisterIncomingEvent
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardCost
import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class RewardRedemption(
    val rewardId: RewardId,
    val user: UserName,
    val cost: RewardCost,
) : TwitchIncomingEvent {
    constructor(
        rewardId: RewardId,
        user: String,
        cost: RewardCost,
    ) : this(
        rewardId,
        UserName(user),
        cost
    )

    override val channel: TwitchChannel get() = rewardId.channel
}
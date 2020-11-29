package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.Reward
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardCost
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption

data class RewardRedemption(
    val reward: Reward,
    val user: User,
    val cost: RewardCost
) : IncomingEvent {
    constructor(event: RewardRedemption) : this(
        event.reward,
        event.user,
        event.cost
    )

    constructor(
        reward: Reward,
        user: String,
        cost: RewardCost
    ) : this(
        reward,
        User(user),
        cost
    )
}
package fr.delphes.bot.event.incoming

import fr.delphes.twitch.model.Reward
import fr.delphes.twitch.model.RewardCost
import fr.delphes.twitch.model.User

data class RewardRedemption(
    val reward: Reward,
    val user: User,
    val cost: RewardCost
) : IncomingEvent {
    constructor(event: fr.delphes.twitch.model.RewardRedemption) : this(
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
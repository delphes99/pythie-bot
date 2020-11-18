package fr.delphes.bot.event.incoming

import fr.delphes.twitch.model.Feature
import fr.delphes.twitch.model.RewardCost
import fr.delphes.twitch.model.User

data class RewardRedemption(
    val feature: Feature,
    val user: User,
    val cost: RewardCost
) : IncomingEvent {
    constructor(event: fr.delphes.twitch.model.RewardRedemption) : this(
        event.feature,
        event.user,
        event.cost
    )

    constructor(
        feature: Feature,
        user: String,
        cost: RewardCost
    ) : this(
        feature,
        User(user),
        cost
    )
}
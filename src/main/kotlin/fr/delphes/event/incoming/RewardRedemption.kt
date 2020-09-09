package fr.delphes.event.incoming

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import fr.delphes.User

data class RewardRedemption(
    val rewardId: String,
    val user: User,
    val cost: RewardCost
): IncomingEvent {
    constructor(event: RewardRedeemedEvent) : this(
        event.redemption.reward.id,
        User(event.redemption.user.displayName),
        event.redemption.reward.cost
    )

    constructor(
        rewardId: String,
        user: String,
        cost: RewardCost
    ) : this(
        rewardId,
        User(user),
        cost
    )
}

typealias RewardCost = Long
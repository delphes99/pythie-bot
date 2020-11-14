package fr.delphes.bot.event.incoming

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import fr.delphes.User

data class RewardRedemption(
    val feature: Feature,
    val user: User,
    val cost: RewardCost
) : IncomingEvent {
    constructor(event: RewardRedeemedEvent) : this(
        Feature(
            event.redemption.reward.id,
            event.redemption.reward.title
        ),
        User(event.redemption.user.displayName),
        event.redemption.reward.cost
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

typealias RewardCost = Long

data class Feature(
    val rewardId: String,
    val rewardTitle: String,
) {
    fun isEquals(featureName: String): Boolean {
        return rewardId == featureName || rewardTitle == featureName
    }
}
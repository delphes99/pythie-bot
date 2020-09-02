package fr.delphes.event.incoming

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent

data class RewardRedemption(
    val rewardId: String,
    val displayName: String
): IncomingEvent {
    constructor(event: RewardRedeemedEvent) : this(event.redemption.reward.id, event.redemption.user.displayName)
}
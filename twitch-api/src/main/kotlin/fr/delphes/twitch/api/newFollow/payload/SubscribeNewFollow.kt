package fr.delphes.twitch.api.newFollow.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeNewFollow(
    override val condition: NewFollowCondition,
    override val transport: SubscribeTransport,
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_FOLLOW,
    override val version: String = "1"
): EventSubSubscribe<NewFollowCondition>
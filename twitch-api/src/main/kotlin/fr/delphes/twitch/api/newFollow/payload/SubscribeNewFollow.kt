package fr.delphes.twitch.api.newFollow.payload

import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeNewFollow(
    val type: EventSubSubscriptionType,
    val version: String = "1",
    val condition: NewFollowCondition,
    val transport: SubscribeTransport
)
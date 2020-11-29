package fr.delphes.twitch.api.streamOnline.payload

import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeStreamOnline(
    val condition: StreamOnlineCondition,
    val transport: SubscribeTransport,
    val type: EventSubSubscriptionType = EventSubSubscriptionType.STREAM_ONLINE,
    val version: String = "1"
)
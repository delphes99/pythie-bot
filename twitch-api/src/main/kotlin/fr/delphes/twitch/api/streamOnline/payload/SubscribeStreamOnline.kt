package fr.delphes.twitch.api.streamOnline.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeStreamOnline(
    override val condition: StreamOnlineCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<StreamOnlineCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.STREAM_ONLINE
}
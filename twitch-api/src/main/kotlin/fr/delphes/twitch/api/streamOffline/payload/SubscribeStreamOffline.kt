package fr.delphes.twitch.api.streamOffline.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeStreamOffline(
    override val condition: StreamOfflineCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<StreamOfflineCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.STREAM_OFFLINE
}
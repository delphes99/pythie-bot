package fr.delphes.twitch.api.channelUpdate.payload

import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelUpdate(
    val condition: ChannelUpdateCondition,
    val transport: SubscribeTransport,
    val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_UPDATE,
    val version: String = "1",
)
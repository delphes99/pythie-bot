package fr.delphes.twitch.eventSub.payload.subscription

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeTransport(
    val method: String = "webhook",
    val callback: String,
    val secret: String
)
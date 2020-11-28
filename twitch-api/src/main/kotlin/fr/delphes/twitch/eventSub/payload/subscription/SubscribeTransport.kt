package fr.delphes.twitch.eventSub.payload.subscription

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeTransport(
    val callback: String,
    val secret: String,
    val method: String = "webhook"
)
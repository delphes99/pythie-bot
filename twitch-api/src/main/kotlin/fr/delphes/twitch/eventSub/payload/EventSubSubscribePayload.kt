package fr.delphes.twitch.eventSub.payload

import kotlinx.serialization.Serializable

@Serializable
data class EventSubSubscribePayload(
    val type: EventSubSubscriptionType,
    val version: String = "1",
    val condition: Condition,
    val transport: Transport
)
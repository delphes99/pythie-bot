package fr.delphes.twitch.eventSub.payload

import kotlinx.serialization.Serializable

@Serializable
data class ListSubscriptionsPayload(
    val total: Long,
    val data: List<EventSubSubscriptionPayload>,
    val limit: Long
)
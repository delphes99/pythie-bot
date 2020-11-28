package fr.delphes.twitch.eventSub.payload.subscription

import kotlinx.serialization.Serializable

@Serializable
data class ListSubscriptionsPayload(
    val total: Long,
    val data: List<SubscriptionPayload>,
    val limit: Long
)
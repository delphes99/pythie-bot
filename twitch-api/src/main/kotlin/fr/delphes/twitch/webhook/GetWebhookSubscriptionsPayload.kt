package fr.delphes.twitch.webhook

import kotlinx.serialization.Serializable

@Serializable
data class GetWebhookSubscriptionsPayload(
    val total: Int,
    val data: List<GetWebhookSubscriptionsDataPayload>
)
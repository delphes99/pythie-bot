package fr.delphes.twitch

import fr.delphes.twitch.eventSub.payload.ListSubscriptionsPayload
import fr.delphes.twitch.webhook.GetWebhookSubscriptionsDataPayload

interface AppHelixApi {
    suspend fun getWebhook(): List<GetWebhookSubscriptionsDataPayload>

    suspend fun unsubscribeWebhook(subscription: GetWebhookSubscriptionsDataPayload)

    suspend fun getEventSubSubscriptions(): ListSubscriptionsPayload
}
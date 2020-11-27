package fr.delphes.twitch

import fr.delphes.twitch.payload.webhook.GetWebhookSubscriptionsDataPayload

interface AppHelixApi {
    suspend fun getWebhook(): List<GetWebhookSubscriptionsDataPayload>

    suspend fun unsubscribeWebhook(subscription: GetWebhookSubscriptionsDataPayload)
}
package fr.delphes.twitch

import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscriptionPayload
import fr.delphes.twitch.webhook.GetWebhookSubscriptionsDataPayload

interface AppHelixApi {
    suspend fun getWebhook(): List<GetWebhookSubscriptionsDataPayload>

    suspend fun unsubscribeWebhook(subscription: GetWebhookSubscriptionsDataPayload)

    suspend fun getEventSubSubscriptions(): ListSubscriptionsPayload

    suspend fun removeEventSubSubscription(subscription: SubscriptionPayload)
}
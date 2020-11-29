package fr.delphes.twitch

import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscriptionPayload
import io.ktor.client.statement.HttpResponse

class AppHelixClient(
    appCredential: TwitchAppCredential
) : AbstractHelixClient(appCredential), AppHelixApi {
    override suspend fun getEventSubSubscriptions(): ListSubscriptionsPayload {
        return "https://api.twitch.tv/helix/eventsub/subscriptions".get(
            appCredential
        )
    }

    override suspend fun removeEventSubSubscription(subscription: SubscriptionPayload) {
        "https://api.twitch.tv/helix/eventsub/subscriptions".delete<HttpResponse>(
            appCredential,
            "id" to subscription.id
        )
    }
}
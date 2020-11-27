package fr.delphes.twitch

import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.webhook.GetWebhookSubscriptionsDataPayload
import fr.delphes.twitch.webhook.GetWebhookSubscriptionsPayload
import fr.delphes.twitch.webhook.SubscribeWebhookMode
import fr.delphes.twitch.webhook.SubscribeWebhookPayload
import io.ktor.client.statement.HttpResponse
import java.time.Duration

class AppHelixClient(
    appCredential: TwitchAppCredential,
    private val webhookSecret: String
) : AbstractHelixClient(appCredential), AppHelixApi {
    override suspend fun getWebhook(): List<GetWebhookSubscriptionsDataPayload> {
        val payload = "https://api.twitch.tv/helix/webhooks/subscriptions".get<GetWebhookSubscriptionsPayload>(
            appCredential
        )

        return payload.data
    }

    override suspend fun unsubscribeWebhook(subscription: GetWebhookSubscriptionsDataPayload) {
        "https://api.twitch.tv/helix/webhooks/hub".post<HttpResponse>(
            SubscribeWebhookPayload(
                subscription.callback,
                SubscribeWebhookMode.unsubscribe,
                subscription.topic,
                Duration.ZERO,
                webhookSecret
            ),
            appCredential
        )
    }
}
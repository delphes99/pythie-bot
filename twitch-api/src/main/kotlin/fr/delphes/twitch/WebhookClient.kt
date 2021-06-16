package fr.delphes.twitch

import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport

class WebhookClient(
    private val publicUrl: String,
    private val user: TwitchUser,
    private val secret: String,
    private val appHelixApi: AppHelixApi,
    private val eventSubConfigurations: List<EventSubConfiguration<*, *>>
) : WebhookApi {
    override suspend fun registerWebhooks() {
        eventSubConfigurations.forEach { configuration ->
            val transport = SubscribeTransport(
                "$publicUrl/${configuration.callback.webhookPath(user.name)}",
                secret
            )
            val subscribePayload = configuration.subscribePayload(user.id, transport)

            appHelixApi.subscribeEventSub(subscribePayload)
        }
    }
}
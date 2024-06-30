package fr.delphes.twitch

import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class WebhookClient(
    private val publicUrl: String,
    private val user: TwitchUser,
    private val secret: String,
    private val appHelixApi: AppHelixApi,
    private val eventSubConfigurations: List<EventSubConfiguration<*, *>>,
) : WebhookApi {
    override suspend fun registerWebhooks() {
        coroutineScope {
            eventSubConfigurations.map { configuration ->
                launch(Dispatchers.IO) {
                    LOGGER.debug { "Register eventsub topic : ${configuration.topic.name}" }

                    try {
                        val transport = SubscribeTransport(
                            "$publicUrl/${configuration.callback.webhookPath(user.name)}",
                            secret
                        )
                        val subscribePayload = configuration.subscribePayload(user.id, transport)

                        appHelixApi.subscribeEventSub(subscribePayload)
                    } catch (e: Exception) {
                        LOGGER.error(e) { "Error while subscribing to topic ${configuration.topic.name}" }
                    }
                }
            }.joinAll()
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}
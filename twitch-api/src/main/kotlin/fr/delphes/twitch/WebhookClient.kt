package fr.delphes.twitch

import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.routing.Routing

class WebhookClient(
    private val publicUrl: String,
    private val channel: String,
    private val userId: String,
    private val secret: String,
    private val channelHelixApi: ChannelHelixApi,
    private val eventSubConfigurations: List<EventSubConfiguration<*, *, *>>
) : WebhookApi {
    private val state = State()

    override fun startWebhooks(routing: Routing) {
        eventSubConfigurations.forEach { configuration ->
            configuration.callback.callbackDefinition(routing, channel)
        }
        state.endStarted = true
    }

    override suspend fun registerWebhooks() {
        if (state.endStarted) {
            eventSubConfigurations.forEach { configuration ->
                val transport = SubscribeTransport(
                    "$publicUrl/${configuration.callback.webhookPath(channel)}",
                    secret
                )
                val subscribePayload = configuration.subscribePayload(userId, transport)

                channelHelixApi.subscribeEventSub(subscribePayload)
            }
        }
    }
}

private data class State(
    var endStarted: Boolean = false
)
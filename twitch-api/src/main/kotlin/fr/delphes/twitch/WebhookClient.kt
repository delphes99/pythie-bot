package fr.delphes.twitch

import fr.delphes.twitch.eventSub.EventSubConfiguration
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
                channelHelixApi.subscribeEventSub(
                    configuration.type,
                    "$publicUrl/${configuration.callback.webhookPath(channel)}",
                    userId,
                    secret
                )
            }
        }
    }
}

private data class State(
    var endStarted: Boolean = false
)
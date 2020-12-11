package fr.delphes.twitch

import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.routing.Routing

class WebhookClient(
    private val publicUrl: String,
    private val user: TwitchUser,
    private val secret: String,
    private val channelHelixApi: ChannelHelixApi,
    private val eventSubConfigurations: List<EventSubConfiguration<*, *, *>>
) : WebhookApi {
    private val state = State()

    override fun startWebhooks(routing: Routing) {
        eventSubConfigurations.forEach { configuration ->
            configuration.callback.callbackDefinition(routing, user.name)
        }
        state.endStarted = true
    }

    override suspend fun registerWebhooks() {
        if (state.endStarted) {
            eventSubConfigurations.forEach { configuration ->
                val transport = SubscribeTransport(
                    "$publicUrl/${configuration.callback.webhookPath(user.name)}",
                    secret
                )
                val subscribePayload = configuration.subscribePayload(user.id, transport)

                channelHelixApi.subscribeEventSub(subscribePayload)
            }
        }
    }
}

private data class State(
    var endStarted: Boolean = false
)
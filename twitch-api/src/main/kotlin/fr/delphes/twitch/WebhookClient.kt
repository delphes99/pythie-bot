package fr.delphes.twitch

import fr.delphes.twitch.api.newFollow.NewFollow
import fr.delphes.twitch.api.newFollow.payload.NewFollowPayload
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext
import mu.KotlinLogging

class WebhookClient(
    private val publicUrl: String,
    private val channel: String,
    private val userId: String,
    private val secret: String,
    private val channelHelixApi: ChannelHelixApi,
    private val listenNewFollow: ((NewFollow) -> Unit)?
) : WebhookApi {
    private val state = State()

    override fun startWebhooks(routing: Routing) {
        //TODO manage duplicate event
        val webhookName = "newFollow"
        routing.post("/$channel/$webhookName") {
            val newFollowPayload = this.call.receive<NewFollowPayload>()
            //TODO  Verify the request signature  to make sure it came from Twitch.
            when {
                newFollowPayload.challenge != null -> {
                    LOGGER.info { "Twitch webhook $webhookName for $channel : Subscription ok" }
                    this.challengeWebHook(newFollowPayload.challenge)
                }
                newFollowPayload.event != null -> {
                    val event = newFollowPayload.event
                    val newFollow = NewFollow(User(event.user_name), newFollowPayload.subscription.created_at)
                    listenNewFollow?.invoke(newFollow)

                    this.context.response.status(HttpStatusCode.OK)
                }
                else -> {
                    LOGGER.error { "Twitch webhook $webhookName for $channel : Unable to handle message" }
                }
            }
        }
        state.endStarted = true
    }

    override suspend fun registerWebhooks() {
        if (state.endStarted) {
            channelHelixApi.subscribeEventSub(
                EventSubSubscriptionType.CHANNEL_FOLLOW,
                "$publicUrl/$channel/newFollow",
                userId,
                secret
            )
        }
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.challengeWebHook(challenge: String) {
        call.respondText(
            challenge,
            ContentType.Text.Html
        )
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}

private data class State(
    var endStarted: Boolean = false
)
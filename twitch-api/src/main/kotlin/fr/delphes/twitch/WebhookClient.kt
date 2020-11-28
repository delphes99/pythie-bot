package fr.delphes.twitch

import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateCondition
import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateEventPayload
import fr.delphes.twitch.api.newFollow.NewFollow
import fr.delphes.twitch.api.newFollow.payload.NewFollowCondition
import fr.delphes.twitch.api.newFollow.payload.NewFollowEventPayload
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
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
    private val listenNewFollow: ((NewFollow) -> Unit)?,
    private val listenChannelUpdate: ((ChannelUpdate) -> Unit)?
) : WebhookApi {
    private val state = State()

    override fun startWebhooks(routing: Routing) {
        //TODO manage duplicate event

        val webhookName2 = "channelUpdate"
        routing.post("/$channel/$webhookName2") {
            val channelUpdatePayload = this.call.receive<NotificationPayload<ChannelUpdateEventPayload, ChannelUpdateCondition>>()
            //TODO  Verify the request signature  to make sure it came from Twitch.
            when {
                channelUpdatePayload.challenge != null -> {
                    LOGGER.info { "Twitch webhook $webhookName2 for $channel : Subscription ok" }
                    this.challengeWebHook(channelUpdatePayload.challenge)
                }
                channelUpdatePayload.event != null -> {
                    val event = channelUpdatePayload.event
                    val channelUpdate = ChannelUpdate(event.title, event.language, event.category_id, event.category_name)
                    listenChannelUpdate?.invoke(channelUpdate)

                    this.context.response.status(HttpStatusCode.OK)
                }
                else -> {
                    LOGGER.error { "Twitch webhook $webhookName2 for $channel : Unable to handle message" }
                }
            }
        }

        val webhookName = "newFollow"
        routing.post("/$channel/$webhookName") {
            val newFollowPayload = this.call.receive<NotificationPayload<NewFollowEventPayload, NewFollowCondition>>()
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
            if(listenNewFollow != null) {
                channelHelixApi.subscribeEventSub(
                    EventSubSubscriptionType.CHANNEL_FOLLOW,
                    "$publicUrl/$channel/newFollow",
                    userId,
                    secret
                )
            }
            if(listenChannelUpdate != null) {
                channelHelixApi.subscribeEventSub(
                    EventSubSubscriptionType.CHANNEL_UPDATE,
                    "$publicUrl/$channel/channelUpdate",
                    userId,
                    secret
                )
            }
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
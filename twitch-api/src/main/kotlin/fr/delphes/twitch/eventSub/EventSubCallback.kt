package fr.delphes.twitch.eventSub

import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext
import mu.KotlinLogging

class EventSubCallback<PAYLOAD, CONDITION : GenericCondition>(
    private val webhookPathSuffix: String,
    private val parse: suspend (ApplicationCall) -> NotificationPayload<PAYLOAD, CONDITION>,
    private val notify: suspend (PAYLOAD) -> Unit
) {
    fun webhookPath(channelName: String) = "$channelName/${webhookPathSuffix}"

    fun callbackDefinition(routing: Routing, channelName: String) {
        routing.post("/${webhookPath(channelName)}") {
            val payload = parse(this.call)
            //TODO Verify the request signature  to make sure it came from Twitch.
            //TODO manage duplicate event
            when {
                payload.challenge != null -> {
                    LOGGER.info { "Twitch webhook $webhookPathSuffix for $channelName : Subscription ok" }
                    this.challengeWebHook(payload.challenge)
                }
                payload.event != null -> {
                    notify(payload.event)

                    this.context.response.status(HttpStatusCode.OK)
                }
                else -> {
                    LOGGER.error { "Twitch webhook $webhookPathSuffix for $channelName : Unable to handle message" }
                }
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
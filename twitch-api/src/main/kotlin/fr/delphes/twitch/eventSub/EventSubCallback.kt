package fr.delphes.twitch.eventSub

import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.util.pipeline.PipelineContext

class EventSubCallback<PAYLOAD, CONDITION : GenericCondition>(
    private val topic: EventSubTopic,
    private val parse: suspend (ApplicationCall) -> NotificationPayload<PAYLOAD, CONDITION>,
) {
    fun webhookPath(channelName: String) = "eventSub/${topic.webhookPathSuffix}/$channelName"

    fun callbackDefinition(routing: Routing, listener: suspend (PAYLOAD) -> Unit) {
        routing.post("/eventSub/${topic.webhookPathSuffix}/{channelName}") {
            val channelName = call.parameters["channelName"].toString()
            val payload = parse(this.call)
            //TODO Verify the request signature  to make sure it came from Twitch.
            //TODO manage duplicate event
            when {
                payload.challenge != null -> {
                    LOGGER.info { "Twitch webhook ${topic.name} for $channelName : Subscription ok" }
                    this.challengeWebHook(payload.challenge)
                }

                payload.event != null -> {
                    listener(payload.event)

                    this.context.respond(HttpStatusCode.OK)
                }

                else -> {
                    LOGGER.error { "Twitch webhook ${topic.webhookPathSuffix} for $channelName : Unable to handle message" }
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
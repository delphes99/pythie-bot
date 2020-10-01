package fr.delphes.bot

import fr.delphes.bot.webserver.webhook.TwitchWebhook
import fr.delphes.storage.serialization.Serializer
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext

class WebServer(
    val bot: ClientBot
) {
    init {
        embeddedServer(Netty, 80) {
            install(ContentNegotiation) {
                json(
                    json = Serializer
                )
            }
            //TODO verify secret send on subscription
            //TODO manage duplicate event
            routing {
                bot.channels.forEach { channel ->
                    TwitchWebhook.forEach { webhook ->
                        get("/${channel.name}/${webhook.callSuffix}") {
                            challengeWebHook()
                        }
                        post("/${channel.name}/${webhook.callSuffix}") {
                            webhook.notificationHandler(channel, this)
                            this.context.response.status(HttpStatusCode.OK)
                        }
                    }
                }
            }
        }.start(wait = false)
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.challengeWebHook() {
        call.respondText(
            this.context.parameters["hub.challenge"] ?: "No challenge provided",
            ContentType.Text.Html
        )
    }
}
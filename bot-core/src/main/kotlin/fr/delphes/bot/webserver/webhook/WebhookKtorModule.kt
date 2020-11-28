package fr.delphes.bot.webserver.webhook

import fr.delphes.bot.Channel
import fr.delphes.bot.ClientBot
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.util.pipeline.PipelineContext
import mu.KotlinLogging

fun Application.WebhookModule(bot: ClientBot) {
    routing {
        bot.channels.forEach { channel ->
            startWebhooks(channel)

            channel.twitchApi.startWebhooks(this)
        }
    }
}

private fun Routing.startWebhooks(channel: Channel) {
    //TODO verify secret send on subscription
    //TODO manage duplicate event
    TwitchWebhook.forEach { webhook ->
        get("/${channel.name}/${webhook.callSuffix}") {
            if(getParameter("hub.mode") == "denied") {
                LOGGER.error { "Twich webhook ${webhook.name} for ${channel.name} : Subscription denied : ${getParameter("hub.reason")}" }
                this.context.response.status(HttpStatusCode.OK)
            } else {
                LOGGER.info { "Twich webhook ${webhook.name} for ${channel.name} : Subscription ok" }
                challengeWebHook()
            }
        }
        post("/${channel.name}/${webhook.callSuffix}") {
            webhook.notificationHandler(channel, this)
            this.context.response.status(HttpStatusCode.OK)
        }
    }
}

private fun PipelineContext<Unit, ApplicationCall>.getParameter(parameterName: String) =
    this.context.parameters[parameterName]

private suspend fun PipelineContext<Unit, ApplicationCall>.challengeWebHook() {
    call.respondText(
        this.context.parameters["hub.challenge"] ?: "No challenge provided",
        ContentType.Text.Html
    )
}

private val LOGGER = KotlinLogging.logger {}
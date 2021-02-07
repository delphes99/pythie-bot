package fr.delphes.connector.twitch.webservice

import fr.delphes.connector.twitch.TwitchConnector
import io.ktor.application.Application
import io.ktor.routing.routing

fun Application.WebhookModule(connector: TwitchConnector) {
    routing {
        connector.clientBot.channels.forEach { channel ->
            channel.twitchApi.startWebhooks(this)
        }
    }
}
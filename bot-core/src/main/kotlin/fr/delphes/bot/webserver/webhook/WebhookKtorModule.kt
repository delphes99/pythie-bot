package fr.delphes.bot.webserver.webhook

import fr.delphes.bot.ClientBot
import io.ktor.application.Application
import io.ktor.routing.routing

//TODO move to twitch connector
fun Application.WebhookModule(bot: ClientBot) {
    routing {
        bot.channels.forEach { channel ->
            channel.twitchApi.startWebhooks(this)
        }
    }
}
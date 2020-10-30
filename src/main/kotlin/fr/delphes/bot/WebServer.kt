package fr.delphes.bot

import fr.delphes.bot.util.serialization.Serializer
import fr.delphes.bot.webserver.admin.FeaturesModule
import fr.delphes.bot.webserver.webhook.WebhookModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class WebServer(
    val bot: ClientBot
) {
    init {
        launchServer(80) {
            WebhookModule(bot)
        }

        launchServer(8080) {
            FeaturesModule(bot)
        }
    }

    private fun launchServer(port: Int, modules: Application.() -> Unit) {
        embeddedServer(Netty, port) {
            install(ContentNegotiation) {
                json(
                    json = Serializer
                )
            }
            modules()
        }.start(wait = false)
    }
}
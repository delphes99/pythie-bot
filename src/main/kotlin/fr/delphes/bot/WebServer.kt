package fr.delphes.bot

import fr.delphes.bot.storage.serialization.Serializer
import fr.delphes.bot.webserver.admin.AdminModule
import fr.delphes.bot.webserver.webhook.WebhookModule
import fr.delphes.feature.statistics.Statistics
import fr.delphes.feature.statistics.StatisticsModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
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
            AdminModule(bot)
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
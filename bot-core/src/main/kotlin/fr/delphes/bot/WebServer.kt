package fr.delphes.bot

import fr.delphes.bot.webserver.admin.AdminModule
import fr.delphes.bot.webserver.alert.AlertModule
import fr.delphes.bot.webserver.auth.AuthInternalModule
import fr.delphes.bot.webserver.auth.AuthModule
import fr.delphes.bot.webserver.webhook.WebhookModule
import fr.delphes.utils.serialization.Serializer
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
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
            AuthModule(bot)
        }

        launchServer(8080) {
            install(CORS) {
                anyHost()
            }
            AdminModule(bot)
            AuthInternalModule(bot)
            AlertModule(bot)
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
package fr.delphes.bot

import fr.delphes.bot.webserver.admin.AdminModule
import fr.delphes.bot.webserver.alert.AlertModule
import fr.delphes.bot.webserver.auth.AuthInternalModule
import fr.delphes.bot.webserver.webhook.WebhookModule
import fr.delphes.utils.serialization.Serializer
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpMethod
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class WebServer(
    val bot: ClientBot,
    internalModules: List<(Application) -> Unit>,
    publicModules: List<(Application) -> Unit>
) {
    init {
        launchServer(80) {
            WebhookModule(bot)
            publicModules.forEach { module -> module(this) }
        }

        launchServer(8080) {
            install(CORS) {
                anyHost()
                allowNonSimpleContentTypes = true
                method(HttpMethod.Delete)
            }
            AdminModule(bot)
            AuthInternalModule(bot)
            AlertModule(bot)
            internalModules.forEach { module -> module(this) }
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

    companion object {
        val FRONT_BASE_URL = "http://localhost:8080"
        //TODO dev profile
        // Local dev
        //val FRONT_BASE_URL = "http://localhost:8082"
    }
}
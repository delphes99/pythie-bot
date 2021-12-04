package fr.delphes.bot

import fr.delphes.bot.connector.state.endpoint.ConnectorsStatus
import fr.delphes.bot.overlay.Overlays
import fr.delphes.bot.webserver.admin.AdminModule
import fr.delphes.bot.webserver.alert.AlertModule
import fr.delphes.bot.webserver.feature.Features
import fr.delphes.bot.webserver.media.MediaModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpMethod
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.json.Json

class WebServer(
    val bot: Bot,
    internalModules: List<(Application) -> Unit>,
    publicModules: List<(Application) -> Unit>
) {
    init {
        launchServer(80, bot.serializer) {
            publicModules.forEach { module -> module(this) }
        }

        launchServer(8080, bot.serializer) {
            install(CORS) {
                anyHost()
                allowNonSimpleContentTypes = true
                method(HttpMethod.Delete)
            }
            AdminModule(bot)
            AlertModule(bot)
            Features(bot)
            MediaModule(bot)
            Overlays(bot)
            ConnectorsStatus(bot)

            internalModules.forEach { module -> module(this) }
        }
    }

    private fun launchServer(port: Int, serializer: Json, modules: Application.() -> Unit) {
        embeddedServer(Netty, port) {
            install(ContentNegotiation) {
                json(
                    json = serializer
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
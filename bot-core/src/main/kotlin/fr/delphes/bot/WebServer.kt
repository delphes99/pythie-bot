package fr.delphes.bot

import fr.delphes.bot.connector.connectionstate.endpoint.ConnectorsModule
import fr.delphes.bot.media.MediaModule
import fr.delphes.bot.monitoring.Monitoring
import fr.delphes.bot.overlay.Overlays
import fr.delphes.bot.webserver.admin.AdminModule
import fr.delphes.dynamicForm.DynamicFormAdminModule
import fr.delphes.feature.web.FeatureAdminModule
import fr.delphes.state.StateModule
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import kotlinx.serialization.json.Json

class WebServer(
    val bot: Bot,
    internalModules: List<(Application) -> Unit>,
    publicModules: List<(Application) -> Unit>,
) {
    init {
        launchServer(80, bot.serializer) {
            publicModules.forEach { module -> module(this) }
        }

        launchServer(8080, bot.serializer) {
            install(CORS) {
                anyHost()
                allowNonSimpleContentTypes = true
                allowMethod(HttpMethod.Delete)
                allowMethod(HttpMethod.Put)
            }
            AdminModule(bot)
            MediaModule(bot.mediaService)
            Overlays(bot)
            ConnectorsModule(bot)
            StateModule(bot)
            DynamicFormAdminModule(bot)
            FeatureAdminModule(bot)
            Monitoring(bot)

            internalModules.forEach { module -> module(this) }
        }
    }

    private fun launchServer(port: Int, serializer: Json, modules: Application.() -> Unit) {
        embeddedServer(Netty, port) {
            install(ContentNegotiation) {
                json(serializer)
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
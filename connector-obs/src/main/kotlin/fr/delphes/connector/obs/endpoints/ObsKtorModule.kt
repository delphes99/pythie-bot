package fr.delphes.connector.obs.endpoints

import fr.delphes.connector.obs.ObsConfiguration
import fr.delphes.connector.obs.ObsConnector
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

fun Application.ObsModule(connector: ObsConnector) {
    routing {
        get("/obs/configuration") {
            val configuration = connector.configuration
            this.call.respond(
                ConfigurationOverview(
                    configuration?.host,
                    configuration?.port,
                )
            )
        }
        post("/obs/configuration") {
            val configuration = this.call.receive<ConfigurationRequest>()
            connector.configure(
                ObsConfiguration(
                    configuration.host,
                    configuration.port,
                    configuration.password?.takeIf { it.isNotBlank() }
                )
            )
            connector.connect()

            this.context.respond(HttpStatusCode.OK)
        }
    }
}

@Serializable
private data class ConfigurationOverview(
    val host: String?,
    val port: Int?,
)

@Serializable
private data class ConfigurationRequest(
    val host: String,
    val port: Int,
    val password: String?,
)
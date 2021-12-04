package fr.delphes.connector.obs.endpoints

import fr.delphes.connector.obs.ObsConfiguration
import fr.delphes.connector.obs.ObsConnector
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
fun Application.ObsModule(connector: ObsConnector) {
    routing {
        get("/obs/configuration") {
            val configuration = connector.state.configuration
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
package fr.delphes.connector.obs.endpoints

import fr.delphes.connector.obs.ObsConfiguration
import fr.delphes.connector.obs.ObsConnector
import fr.delphes.connector.obs.ObsState
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.Serializable

fun Application.ObsModule(connector: ObsConnector) {
    routing {
        get("/obs/status") {
            this.context.respond(connector.state.toStatus())
        }
        post("/obs/configuration") {
            val configuration = this.call.receive<DiscordConfigurationRequest>()
            connector.configure(
                ObsConfiguration(
                    configuration.url,
                    configuration.password?.takeIf { it.isNotBlank() }
                )
            )
            connector.connect()

            this.context.respond(HttpStatusCode.OK)
        }
    }
}

@Serializable
private data class DiscordConfigurationRequest(
    val url: String,
    val password: String?,
)

private fun ObsState.toStatus(): ObsStatus {
    val status = when (this) {
        ObsState.Unconfigured -> ObsStatusEnum.unconfigured
        is ObsState.Configured -> ObsStatusEnum.configured
        is ObsState.Error -> ObsStatusEnum.error
        is ObsState.Connected -> ObsStatusEnum.connected
    }
    return ObsStatus(status)
}
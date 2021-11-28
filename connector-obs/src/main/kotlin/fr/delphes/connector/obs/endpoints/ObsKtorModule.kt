package fr.delphes.connector.obs.endpoints

import fr.delphes.bot.connector.state.Configured
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.Connecting
import fr.delphes.bot.connector.state.ConnectorState
import fr.delphes.bot.connector.state.Disconnecting
import fr.delphes.bot.connector.state.InError
import fr.delphes.bot.connector.state.NotConfigured
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
        get("/obs/status") {
            this.context.respond(connector.stateMachine.state.toStatus())
        }
        post("/obs/configuration") {
            val configuration = this.call.receive<DiscordConfigurationRequest>()
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
private data class DiscordConfigurationRequest(
    val host: String,
    val port: Int,
    val password: String?,
)

@InternalSerializationApi
private fun ConnectorState<*, *>.toStatus(): ObsStatus {
    val status = when (this) {
        is Configured -> ObsStatusEnum.configured
        is Connected -> ObsStatusEnum.connected
        is Connecting -> ObsStatusEnum.connected //TODO
        is Disconnecting -> ObsStatusEnum.configured //TODO
        is InError -> ObsStatusEnum.error
        is NotConfigured -> ObsStatusEnum.unconfigured
    }
    return ObsStatus(status)
}
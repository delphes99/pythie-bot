package fr.delphes.bot.connector.state.endpoint

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.state.Configured
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.Connecting
import fr.delphes.bot.connector.state.ConnectorState
import fr.delphes.bot.connector.state.Disconnecting
import fr.delphes.bot.connector.state.InError
import fr.delphes.bot.connector.state.NotConfigured
import io.ktor.application.Application
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.ConnectorsStatus(bot: Bot) {
    routing {
        get("/connectors/status") {
            this.context.respond(
                bot.connectors.map { ConnectorStatus(it.connectorName, it.state.toStatus()) }
            )
        }
    }
}

private fun ConnectorState<*, *>.toStatus(): ConnectorStatusEnum {
    return when (this) {
        is Configured -> ConnectorStatusEnum.configured
        is Connected -> ConnectorStatusEnum.connected
        is Connecting -> ConnectorStatusEnum.connecting
        is Disconnecting -> ConnectorStatusEnum.disconnecting
        is InError -> ConnectorStatusEnum.inError
        is NotConfigured -> ConnectorStatusEnum.unconfigured
    }
}
package fr.delphes.bot.connector.state.endpoint

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorStatus
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import fr.delphes.bot.connector.state.endpoint.ConnectorStatus as ConnectorStatusOutput

fun Application.ConnectorsModule(bot: Bot) {
    routing {
        get("/connectors/status") {
            this.context.respond(
                bot.connectors.map { ConnectorStatusOutput(it.connectorName, it.status.toStatus()) }
            )
        }
        actionsOnConnector(
            bot,
            "/connectors/{connector}/connect" activate { connect() },
            "/connectors/{connector}/disconnect" activate { disconnect() },
        )
    }
}

private fun Routing.actionsOnConnector(
    bot: Bot,
    vararg actions: Pair<String, ActionOnConnector>
) {
    actions.forEach { (url, actionOnConnector) ->
        post(url) {
            call.respond(
                bot
                    .findConnector(call.parameters["connector"])
                    ?.also { it.actionOnConnector() }
                    ?.let { HttpStatusCode.OK }
                    ?: HttpStatusCode.NotFound
            )
        }
    }
}

typealias ActionOnConnector = suspend Connector<*, *>.() -> Unit

private infix fun String.activate(action: ActionOnConnector) = Pair(this, action)

private fun ConnectorStatus.toStatus(): ConnectorStatusEnum {
    return when (this) {
        ConnectorStatus.Configured -> ConnectorStatusEnum.configured
        ConnectorStatus.Connected -> ConnectorStatusEnum.connected
        ConnectorStatus.Connecting -> ConnectorStatusEnum.connecting
        ConnectorStatus.Disconnecting -> ConnectorStatusEnum.disconnecting
        ConnectorStatus.InError -> ConnectorStatusEnum.inError
        ConnectorStatus.NotConfigured -> ConnectorStatusEnum.unconfigured
    }
}
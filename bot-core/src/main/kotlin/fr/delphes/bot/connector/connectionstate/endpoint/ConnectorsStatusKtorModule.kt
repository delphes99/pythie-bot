package fr.delphes.bot.connector.connectionstate.endpoint

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.status.toOutput
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.ConnectorsModule(bot: Bot) {
    routing {
        get("/connectors/status") {
            this.context.respond(
                bot.connectors.map { toOutput(it.connectorName, it.status) }
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
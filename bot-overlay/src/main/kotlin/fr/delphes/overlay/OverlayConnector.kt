package fr.delphes.overlay

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorState
import fr.delphes.bot.connector.ConnectorType
import fr.delphes.overlay.event.outgoing.Alert
import fr.delphes.overlay.event.outgoing.PlaySound
import fr.delphes.overlay.webserver.AlertModule
import io.ktor.server.application.Application
import kotlinx.coroutines.channels.Channel

class OverlayConnector(
    private val bot: Bot,
) : Connector<OverlayConfiguration, OverlayRuntime> {
    override val connectorType = ConnectorType("Overlay")
    override val states = emptyList<ConnectorState>()
    override val configurationManager = OverlayConfigurationManager()
    override val connectionManager = OverlayConnectionManager(this)

    override val outgoingEventsTypes = listOf(
        PlaySound.builderDefinition
    )

    override val botConfiguration = bot.configuration

    //TODO move to runtime ?
    val alerts = Channel<Alert>()


    override fun internalEndpoints(application: Application) {
        application.AlertModule(alerts, bot)
    }

    override fun publicEndpoints(application: Application) {

    }
}
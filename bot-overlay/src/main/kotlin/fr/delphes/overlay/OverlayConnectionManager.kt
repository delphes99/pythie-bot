package fr.delphes.overlay

import fr.delphes.bot.connector.StandAloneConnectionManager
import fr.delphes.bot.connector.connectionstate.Connected
import fr.delphes.bot.connector.connectionstate.ConnectionSuccessful
import fr.delphes.bot.connector.connectionstate.ConnectorTransition
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.overlay.event.outgoing.Alert
import fr.delphes.overlay.event.outgoing.OverlayOutgoingEvent
import fr.delphes.overlay.event.outgoing.Pause
import fr.delphes.overlay.event.outgoing.PlaySound
import kotlinx.coroutines.delay

class OverlayConnectionManager(
    private val connector: OverlayConnector,
) : StandAloneConnectionManager<OverlayConfiguration, OverlayRuntime>(
    connector.configurationManager
) {
    override val connectionName = "Overlay"

    override suspend fun doConnection(configuration: OverlayConfiguration): ConnectorTransition<OverlayConfiguration, OverlayRuntime> {
        return ConnectionSuccessful(
            configuration,
            OverlayRuntime()
        )
    }

    override suspend fun execute(event: OutgoingEvent) {
        if (event is OverlayOutgoingEvent) {
            val currentState = state
            if (currentState is Connected) {
                when (event) {
                    is Alert -> connector.alerts.send(event)
                    is Pause -> delay(event.delay.toMillis())
                    is PlaySound -> connector.alerts.send(
                        Alert(
                            "playSound",
                            "mediaName" to event.mediaName,
                        )
                    )
                }
            }
        }
    }
}
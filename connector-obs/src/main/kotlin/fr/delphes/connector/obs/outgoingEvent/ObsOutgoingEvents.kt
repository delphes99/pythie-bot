package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.ObsConnector
import fr.delphes.obs.request.Position
import fr.delphes.obs.request.SetSceneItemProperties
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
sealed class ObsOutgoingEvent : OutgoingEvent {
    abstract suspend fun executeOnObs(connector: ObsConnector)
}

@InternalSerializationApi
data class ChangeItemVisibility(
    val itemName: String,
    val visible: Boolean
) : ObsOutgoingEvent() {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSceneItemProperties(
                    item = itemName,
                    visible = visible
                )
            )
        }
    }
}

@InternalSerializationApi
data class ChangeItemPosition(
    val itemName: String,
    val x: Double,
    val y: Double,
) : ObsOutgoingEvent() {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSceneItemProperties(
                    item = itemName,
                    position = Position(x, y)
                )
            )
        }
    }
}

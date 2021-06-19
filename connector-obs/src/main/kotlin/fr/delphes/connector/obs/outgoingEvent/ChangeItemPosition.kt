package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.connector.obs.ObsConnector
import fr.delphes.obs.request.Position
import fr.delphes.obs.request.SetSceneItemProperties
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
data class ChangeItemPosition(
    val itemName: String,
    val x: Double,
    val y: Double,
    val sceneName: String? = null,
) : ObsOutgoingEvent {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSceneItemProperties(
                    item = itemName,
                    sceneName = sceneName,
                    position = Position(x, y)
                )
            )
        }
    }
}
package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.connector.obs.ObsConnector
import fr.delphes.obs.request.SetSceneItemProperties
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
data class ChangeItemVisibility(
    val itemName: String,
    val visible: Boolean,
    val sceneName: String? = null,
) : ObsOutgoingEvent {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSceneItemProperties(
                    item = itemName,
                    sceneName = sceneName,
                    visible = visible
                )
            )
        }
    }
}
package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.connector.obs.ObsConnector
import fr.delphes.obs.request.SetSceneItemProperties
import kotlinx.coroutines.delay
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
data class RefreshSource(
    val sceneName: String,
    val sourceName: String,
) : ObsOutgoingEvent {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSceneItemProperties(
                    sceneName = sceneName,
                    item = sourceName,
                    visible = false
                )
            )
            delay(50)
            client.sendRequest(
                SetSceneItemProperties(
                    sceneName = sceneName,
                    item = sourceName,
                    visible = true
                )
            )
        }
    }
}
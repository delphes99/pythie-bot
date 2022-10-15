package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.connector.obs.ObsConnector
import fr.delphes.obs.toObs.request.GetSceneItemList
import fr.delphes.obs.toObs.request.SetSceneItemEnabled
import kotlinx.coroutines.delay
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
data class RefreshSource(
    val sceneName: String,
    //TODO item name > item id
    val itemId: Long,
) : ObsOutgoingEvent {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            //TODO item name > item id
            client.sendRequest(
                GetSceneItemList(sceneName)
            )
            client.sendRequest(
                SetSceneItemEnabled(
                    sceneName = sceneName,
                    sceneItemId = itemId,
                    sceneItemEnabled = false
                )
            )
            delay(50)
            client.sendRequest(
                SetSceneItemEnabled(
                    sceneName = sceneName,
                    sceneItemId = itemId,
                    sceneItemEnabled = true
                )
            )
        }
    }
}
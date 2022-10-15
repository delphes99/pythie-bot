package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.connector.obs.ObsConnector
import fr.delphes.obs.toObs.request.GetSceneItemList
import fr.delphes.obs.toObs.request.SetSceneItemEnabled
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
data class ChangeItemVisibility(
    //TODO item name > item id
    val itemId: Long,
    val visible: Boolean,
    val sceneName: String,
) : ObsOutgoingEvent {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                GetSceneItemList(sceneName)
            )
            client.sendRequest(
                SetSceneItemEnabled(
                    sceneName = sceneName,
                    sceneItemId = itemId,
                    sceneItemEnabled = visible
                )
            )
        }
    }
}
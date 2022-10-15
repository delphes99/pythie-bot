package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.connector.obs.ObsConnector
import fr.delphes.obs.toObs.request.GetSceneItemList
import fr.delphes.obs.toObs.request.SceneItemTransform
import fr.delphes.obs.toObs.request.SetSceneItemTransform
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
data class ChangeItemPosition(
    //TODO item name > item id
    val itemName: Long,
    val x: Double,
    val y: Double,
    val sceneName: String,
) : ObsOutgoingEvent {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            //TODO item name > item id
            client.sendRequest(
                GetSceneItemList(sceneName)
            )
            client.sendRequest(
                SetSceneItemTransform(sceneName, itemName, SceneItemTransform(x, y))
            )
        }
    }
}
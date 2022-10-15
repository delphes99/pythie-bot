package fr.delphes.obs.toObs.request

import kotlinx.serialization.Serializable

@Serializable
data class GetSceneItemList(
    val sceneName: String
) : RequestDataPayload() {
    override val requestType = "GetSceneItemList"
}
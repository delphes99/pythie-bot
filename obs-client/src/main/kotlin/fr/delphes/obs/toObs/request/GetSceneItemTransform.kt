package fr.delphes.obs.toObs.request

import kotlinx.serialization.Serializable

@Serializable
data class GetSceneItemTransform(
    val sceneName: String,
    val sceneItemId: Long,
) : RequestDataPayload() {
    override val requestType = "GetSceneItemTransform"
}
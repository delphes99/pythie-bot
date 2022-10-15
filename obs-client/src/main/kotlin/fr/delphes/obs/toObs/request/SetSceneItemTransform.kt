package fr.delphes.obs.toObs.request

import kotlinx.serialization.Serializable

@Serializable
data class SetSceneItemTransform(
    val sceneName: String,
    val sceneItemId: Long,
    val sceneItemTransform: SceneItemTransform
) : RequestDataPayload() {
    override val requestType = "SetSceneItemTransform"
}

@Serializable
data class SceneItemTransform(
    val positionX: Double,
    val positionY: Double,
)

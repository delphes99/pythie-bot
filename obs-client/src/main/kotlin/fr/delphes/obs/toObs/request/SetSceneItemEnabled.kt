package fr.delphes.obs.toObs.request

import kotlinx.serialization.Serializable

@Serializable
data class SetSceneItemEnabled(
    val sceneName: String,
    val sceneItemId: Long,
    val sceneItemEnabled: Boolean,
) : RequestDataPayload() {
    override val requestType = "SetSceneItemEnabled"
}
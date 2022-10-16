package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SceneItemEnableStateChanged")
data class SceneItemEnableStateChanged(
    override val eventIntent: Long,
    override val eventData: SceneItemEnableStateChangedData
): EventData() {
    override val eventType: String = "SceneItemEnableStateChanged"
}

@Serializable
data class SceneItemEnableStateChangedData(
    val sceneItemEnabled: Boolean,
    val sceneItemId: Long,
    val sceneName: String,
)
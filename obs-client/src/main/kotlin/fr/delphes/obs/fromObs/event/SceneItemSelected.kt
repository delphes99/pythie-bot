package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SceneItemSelected")
data class SceneItemSelected(
    override val eventIntent: Long,
    override val eventData: SceneItemSelectedData
) : EventData() {
    override val eventType: String = "SceneItemSelected"
}

@Serializable
data class SceneItemSelectedData(
    val sceneItemId: Long,
    val sceneName: String
)
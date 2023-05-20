package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SceneItemRemoved")
data class SceneItemRemoved(
    override val eventIntent: Long,
    override val eventData: SceneItemRemovedData,
) : EventData() {
    override val eventType: String = "SceneItemRemoved"
}

@Serializable
data class SceneItemRemovedData(
    val sceneItemId: Long,
    val sceneName: String,
    val sourceName: String,
)
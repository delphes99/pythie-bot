package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SceneItemCreated")
data class SceneItemCreated(
    override val eventIntent: Long,
    override val eventData: SceneItemCreatedData,
) : EventData() {
    override val eventType: String = "SceneItemCreated"
}

@Serializable
data class SceneItemCreatedData(
    val sceneItemId: Long,
    val sceneItemIndex: Long,
    val sceneName: String,
    val sourceName: String,
)
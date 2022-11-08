package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SceneRemoved")
data class SceneRemoved(
    override val eventIntent: Long,
    override val eventData: SceneRemovedData
) : EventData() {
    override val eventType: String = "SceneRemoved"
}

@Serializable
data class SceneRemovedData(
    val isGroup: Boolean,
    val sceneName: String
)
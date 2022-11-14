package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SceneTransitionStarted")
data class SceneTransitionStarted(
    override val eventIntent: Long,
    override val eventData: SceneTransitionStartedData
) : EventData() {
    override val eventType: String = "SceneTransitionStarted"
}

@Serializable
data class SceneTransitionStartedData(
    val transitionName: String
)
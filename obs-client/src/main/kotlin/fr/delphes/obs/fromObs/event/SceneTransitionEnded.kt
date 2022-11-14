package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SceneTransitionEnded")
data class SceneTransitionEnded(
    override val eventIntent: Long,
    override val eventData: SceneTransitionEndedData
) : EventData() {
    override val eventType: String = "SceneTransitionEnded"
}

@Serializable
data class SceneTransitionEndedData(
    val transitionName: String
)
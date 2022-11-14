package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SceneTransitionVideoEnded")
data class SceneTransitionVideoEnded(
    override val eventIntent: Long,
    override val eventData: SceneTransitionVideoEndedData
) : EventData() {
    override val eventType: String = "SceneTransitionVideoEnded"
}

@Serializable
data class SceneTransitionVideoEndedData(
    val transitionName: String
)
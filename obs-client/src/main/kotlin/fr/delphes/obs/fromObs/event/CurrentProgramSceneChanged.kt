package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CurrentProgramSceneChanged")
data class CurrentProgramSceneChanged(
    override val eventIntent: Long,
    override val eventData: CurrentProgramSceneChangedData
) : EventData() {
    override val eventType: String = "CurrentProgramSceneChanged"
}

@Serializable
data class CurrentProgramSceneChangedData(
    val sceneName: String
)
package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("StudioModeStateChanged")
data class StudioModeStateChanged(
    override val eventIntent: Long,
    override val eventData: StudioModeStateChangedData,
) : EventData() {
    override val eventType: String = "StudioModeStateChanged"
}

@Serializable
data class StudioModeStateChangedData(
    val studioModeEnabled: Boolean,
)
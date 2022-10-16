package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SourceFilterEnableStateChanged")
data class SourceFilterEnableStateChanged(
    override val eventIntent: Long,
    override val eventData: SourceFilterEnableStateChangedData
) : EventData() {
    override val eventType: String = "SourceFilterEnableStateChanged"
}

@Serializable
data class SourceFilterEnableStateChangedData(
    val filterEnabled: Boolean,
    val filterName: String,
    val sourceName: String,
)
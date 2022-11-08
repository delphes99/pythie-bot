package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SourceFilterRemoved")
data class SourceFilterRemoved(
    override val eventIntent: Long,
    override val eventData: SourceFilterRemovedData
) : EventData() {
    override val eventType: String = "SourceFilterRemoved"
}

@Serializable
data class SourceFilterRemovedData(
    val filterName: String,
    val sourceName: String,
)
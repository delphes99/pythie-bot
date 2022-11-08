package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ExitStarted")
data class ExitStarted(
    override val eventIntent: Long,
) : EventData() {
    override val eventType: String = "ExitStarted"
    override val eventData: EmptyEventData? = null
}

@Serializable
data class ExitStartedData(
    val inputName: String,
)
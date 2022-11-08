package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("InputRemoved")
data class InputRemoved(
    override val eventIntent: Long,
    override val eventData: InputRemovedData
) : EventData() {
    override val eventType: String = "InputRemoved"
}

@Serializable
data class InputRemovedData(
    val inputName: String
)
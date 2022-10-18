package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("InputMuteStateChanged")
data class InputMuteStateChanged(
    override val eventIntent: Long,
    override val eventData: InputMuteStateChangedData
) : EventData() {
    override val eventType: String = "InputMuteStateChanged"
}

@Serializable
data class InputMuteStateChangedData(
    val inputMuted: Boolean,
    val inputName: String
)
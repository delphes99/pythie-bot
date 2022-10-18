package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("InputVolumeChanged")
data class InputVolumeChanged(
    override val eventIntent: Long,
    override val eventData: InputVolumeChangedData
) : EventData() {
    override val eventType: String = "InputVolumeChanged"
}

@Serializable
data class InputVolumeChangedData(
    val inputName: String,
    val inputVolumeDb: Double,
    val inputVolumeMul: Double
)
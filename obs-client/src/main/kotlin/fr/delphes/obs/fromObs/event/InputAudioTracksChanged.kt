package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("InputAudioTracksChanged")
data class InputAudioTracksChanged(
    override val eventIntent: Long,
    override val eventData: InputAudioTracksChangedData
) : EventData() {
    override val eventType: String = "InputAudioTracksChanged"
}

@Serializable
data class InputAudioTracksChangedData(
    val inputName: String,
    val inputAudioTracks: InputAudioTracks
)

@Serializable
data class InputAudioTracks(
    @SerialName("1")
    val track1: Boolean,
    @SerialName("2")
    val track2: Boolean,
    @SerialName("3")
    val track3: Boolean,
    @SerialName("4")
    val track4: Boolean,
    @SerialName("5")
    val track5: Boolean,
    @SerialName("6")
    val track6: Boolean,
)
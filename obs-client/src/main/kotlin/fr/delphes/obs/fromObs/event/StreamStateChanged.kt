package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("StreamStateChanged")
data class StreamStateChanged(
    override val eventIntent: Long,
    override val eventData: StreamStateChangedData
) : EventData() {
    override val eventType: String = "StreamStateChanged"
}

@Serializable
data class StreamStateChangedData(
    val outputActive: Boolean,
    val outputState: StreamStateChangedState
)

@Serializable
enum class StreamStateChangedState {
    @SerialName("OBS_WEBSOCKET_OUTPUT_UNKNOWN")
    UNKNOWN,
    @SerialName("OBS_WEBSOCKET_OUTPUT_STARTING")
    STARTING,
    @SerialName("OBS_WEBSOCKET_OUTPUT_STARTED")
    STARTED,
    @SerialName("OBS_WEBSOCKET_OUTPUT_STOPPING")
    STOPPING,
    @SerialName("OBS_WEBSOCKET_OUTPUT_STOPPED")
    STOPPED,
}

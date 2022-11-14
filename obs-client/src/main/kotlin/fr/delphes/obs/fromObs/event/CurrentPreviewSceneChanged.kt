package fr.delphes.obs.fromObs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CurrentPreviewSceneChanged")
data class CurrentPreviewSceneChanged(
    override val eventIntent: Long,
    override val eventData: CurrentPreviewSceneChangedData
) : EventData() {
    override val eventType: String = "CurrentPreviewSceneChanged"
}

@Serializable
data class CurrentPreviewSceneChangedData(
    val sceneName: String
)
package fr.delphes.obs.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Event

@Serializable
data class SwitchScenes(
    @SerialName("scene-name")
    val sceneName: String
): Event() {
    @SerialName("update-type")
    val updateType: String = "SwitchScenes"
}
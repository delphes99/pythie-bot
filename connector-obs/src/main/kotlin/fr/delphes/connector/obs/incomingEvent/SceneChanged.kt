package fr.delphes.connector.obs.incomingEvent

import kotlinx.serialization.Serializable

@Serializable
data class SceneChanged(
    val newScene: String,
) : ObsIncomingEvent
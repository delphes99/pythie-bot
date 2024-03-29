package fr.delphes.connector.obs.incomingEvent

import fr.delphes.annotation.incomingEvent.RegisterIncomingEvent
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class SceneChanged(
    val newScene: String,
) : ObsIncomingEvent
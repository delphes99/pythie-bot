package fr.delphes.connector.obs.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent
import kotlinx.serialization.Serializable

@Serializable
data class SceneChanged(
    val newScene: String,
) : IncomingEvent
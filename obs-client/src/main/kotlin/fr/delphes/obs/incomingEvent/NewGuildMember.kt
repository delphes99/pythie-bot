package fr.delphes.obs.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent

data class SceneChanged(
    val newScene: String
) : IncomingEvent
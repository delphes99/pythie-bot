package fr.delphes.features.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandler
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.NonEditableFeature
import fr.delphes.connector.obs.incomingEvent.SceneChanged

class SceneChanged(
    val sceneChanged: (SceneChanged) -> List<OutgoingEvent>
) : NonEditableFeature {
    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(SceneChangedHandler())
        .build()

    inner class SceneChangedHandler : LegacyEventHandler<SceneChanged> {
        override suspend fun handle(event: SceneChanged, bot: Bot): List<OutgoingEvent> =
            sceneChanged(event)
    }
}
package fr.delphes.features.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.NonEditableFeature
import fr.delphes.obs.incomingEvent.SceneChanged

class SceneChanged(
    val sceneChanged: (SceneChanged) -> List<OutgoingEvent>
) : NonEditableFeature<SceneChangedDescription> {
    override fun description() = SceneChangedDescription()

    override val eventHandlers = run {
        val handlers = EventHandlers()
        handlers.addHandler(SceneChangedHandler())
        handlers
    }

    inner class SceneChangedHandler : EventHandler<SceneChanged> {
        override suspend fun handle(event: SceneChanged, bot: Bot): List<OutgoingEvent> =
            sceneChanged(event)
    }
}
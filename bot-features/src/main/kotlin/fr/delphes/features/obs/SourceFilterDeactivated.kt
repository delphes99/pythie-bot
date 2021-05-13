package fr.delphes.features.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.incomingEvent.SourceFilterDeactivated as ObsSourceFilterDeactivated
import fr.delphes.feature.NonEditableFeature

class SourceFilterDeactivated(
    val sourceFilterDeactivated: (ObsSourceFilterDeactivated) -> List<OutgoingEvent>
) : NonEditableFeature<SourceFilterDeactivatedDescription> {
    override fun description() = SourceFilterDeactivatedDescription()

    override val eventHandlers = run {
        val handlers = EventHandlers()
        handlers.addHandler(SourceFilterDeactivatedHandler())
        handlers
    }

    inner class SourceFilterDeactivatedHandler : EventHandler<ObsSourceFilterDeactivated> {
        override suspend fun handle(event: ObsSourceFilterDeactivated, bot: Bot): List<OutgoingEvent> =
            sourceFilterDeactivated(event)
    }
}
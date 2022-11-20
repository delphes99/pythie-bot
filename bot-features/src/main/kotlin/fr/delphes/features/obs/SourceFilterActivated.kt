package fr.delphes.features.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.incomingEvent.SourceFilterActivated as ObsSourceFilterActivated
import fr.delphes.feature.NonEditableFeature

class SourceFilterActivated(
    val sourceFilterActivated: (ObsSourceFilterActivated) -> List<OutgoingEvent>
) : NonEditableFeature<SourceFilterActivatedDescription> {
    override fun description() = SourceFilterActivatedDescription()

    override val eventHandlers = EventHandlers
        .builder()
        .addHandler(SourceFilterActivatedHandler())
        .build()

    inner class SourceFilterActivatedHandler : EventHandler<ObsSourceFilterActivated> {
        override suspend fun handle(event: ObsSourceFilterActivated, bot: Bot): List<OutgoingEvent> =
            sourceFilterActivated(event)
    }
}
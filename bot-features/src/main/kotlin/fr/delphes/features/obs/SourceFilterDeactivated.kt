package fr.delphes.features.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandler
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.incomingEvent.SourceFilterDeactivated as ObsSourceFilterDeactivated
import fr.delphes.feature.NonEditableFeature

class SourceFilterDeactivated(
    val sourceFilterDeactivated: (ObsSourceFilterDeactivated) -> List<OutgoingEvent>
) : NonEditableFeature<SourceFilterDeactivatedDescription> {
    override fun description() = SourceFilterDeactivatedDescription()

    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(SourceFilterDeactivatedHandler())
        .build()

    inner class SourceFilterDeactivatedHandler : LegacyEventHandler<ObsSourceFilterDeactivated> {
        override suspend fun handle(event: ObsSourceFilterDeactivated, bot: Bot): List<OutgoingEvent> =
            sourceFilterDeactivated(event)
    }
}
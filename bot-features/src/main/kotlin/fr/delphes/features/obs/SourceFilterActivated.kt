package fr.delphes.features.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandler
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.incomingEvent.SourceFilterActivated as ObsSourceFilterActivated
import fr.delphes.feature.NonEditableFeature

class SourceFilterActivated(
    val sourceFilterActivated: (ObsSourceFilterActivated) -> List<OutgoingEvent>
) : NonEditableFeature<SourceFilterActivatedDescription> {
    override fun description() = SourceFilterActivatedDescription()

    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(SourceFilterActivatedHandler())
        .build()

    inner class SourceFilterActivatedHandler : LegacyEventHandler<ObsSourceFilterActivated> {
        override suspend fun handle(event: ObsSourceFilterActivated, bot: Bot): List<OutgoingEvent> =
            sourceFilterActivated(event)
    }
}
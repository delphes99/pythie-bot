package fr.delphes.features.core.botStarted

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandler
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.incoming.BotStarted
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.NonEditableFeature

class BotStarted(
    val listener: () -> List<OutgoingEvent>
) : NonEditableFeature<BotStartedDescription> {
    override fun description() = BotStartedDescription()

    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(BotStartedHandler())
        .build()

    inner class BotStartedHandler : LegacyEventHandler<BotStarted> {
        override suspend fun handle(event: BotStarted, bot: Bot): List<OutgoingEvent> =
            listener()
    }
}
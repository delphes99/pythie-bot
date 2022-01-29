package fr.delphes.features.core.botStarted

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.BotStarted
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.NonEditableFeature

class BotStarted(
    val listener: () -> List<OutgoingEvent>
) : NonEditableFeature<BotStartedDescription> {
    override fun description() = BotStartedDescription()

    override val eventHandlers = run {
        val handlers = EventHandlers()
        handlers.addHandler(BotStartedHandler())
        handlers
    }

    inner class BotStartedHandler : EventHandler<BotStarted> {
        override suspend fun handle(event: BotStarted, bot: Bot): List<OutgoingEvent> =
            listener()
    }
}
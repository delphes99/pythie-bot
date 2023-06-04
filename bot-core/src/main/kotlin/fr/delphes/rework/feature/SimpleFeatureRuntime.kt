package fr.delphes.rework.feature

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import fr.delphes.utils.uuid.uuid

data class SimpleFeatureRuntime(
    val eventHandlers: EventHandlers,
    override val id: FeatureId = FeatureId(uuid()),
) : FeatureRuntime {
    override suspend fun handleIncomingEvent(event: IncomingEventWrapper<out IncomingEvent>, bot: Bot) {
        eventHandlers.handleEvent(event, bot)
    }

    companion object {
        inline fun <reified T : IncomingEvent> whichHandle(
            id: FeatureId = FeatureId(uuid()),
            noinline action: IncomingEventHandlerAction<T>,
        ): SimpleFeatureRuntime {
            val eventHandlers = EventHandlers.of<T>(action)

            return SimpleFeatureRuntime(eventHandlers, id)
        }
    }
}
package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent

fun interface EventHandler<T : IncomingEvent> {
    suspend fun handle(event: T, bot: Bot)

    companion object {
        inline fun <reified T : IncomingEvent> of(noinline handler: EventHandlerAction<T>) =
            EventHandler { event: T, bot: Bot ->
                EventHandlerContext(bot, event, bot.featuresManager.stateManager).handler()
            }
    }
}
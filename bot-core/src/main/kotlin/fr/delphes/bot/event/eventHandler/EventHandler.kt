package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper

fun interface EventHandler<T : IncomingEvent> {
    suspend fun handle(event: IncomingEventWrapper<T>, bot: Bot)

    companion object {
        inline fun <reified T : IncomingEvent> of(noinline handler: IncomingEventHandlerAction<T>) =
            EventHandler { event: IncomingEventWrapper<T>, bot: Bot ->
                EventHandlerContext(bot, event, bot.stateManager).handler()
            }
    }
}
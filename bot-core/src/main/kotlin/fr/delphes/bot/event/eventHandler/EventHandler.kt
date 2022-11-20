package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent

fun interface EventHandler<T : IncomingEvent> {
    suspend fun handle(event: T, bot: Bot)
}
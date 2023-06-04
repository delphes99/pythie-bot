package fr.delphes.bot

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper

interface IncomingEventHandler {
    suspend fun handle(incomingEvent: IncomingEventWrapper<out IncomingEvent>)
}
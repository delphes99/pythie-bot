package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

interface EventHandler<T : IncomingEvent> {
    suspend fun handle(event: T, channel: ChannelInfo): List<OutgoingEvent>
}
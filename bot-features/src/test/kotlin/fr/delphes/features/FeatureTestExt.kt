package fr.delphes.features

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.Feature

suspend fun <T: IncomingEvent> Feature.handle(event: T, channelInfo: ChannelInfo): List<OutgoingEvent> {
    val eventHandlers = EventHandlers()
    this.registerHandlers(eventHandlers = eventHandlers)

    return eventHandlers.handleEvent(event, channelInfo)
}
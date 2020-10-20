package fr.delphes.feature

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.OutgoingEvent

fun Feature.handle(commandAsked: CommandAsked, channelInfo: ChannelInfo): List<OutgoingEvent> {
    val eventHandlers = EventHandlers()
    this.registerHandlers(eventHandlers = eventHandlers)

    return eventHandlers.handleEvent(commandAsked, channelInfo)
}
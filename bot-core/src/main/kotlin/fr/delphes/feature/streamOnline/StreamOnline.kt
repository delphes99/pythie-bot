package fr.delphes.feature.streamOnline

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class StreamOnline(
    val streamOnlineResponse: (StreamOnline) -> List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(StreamOnlineHandler())
    }

    inner class StreamOnlineHandler : EventHandler<StreamOnline> {
        override suspend fun handle(event: StreamOnline, channel: ChannelInfo): List<OutgoingEvent> = streamOnlineResponse(event)
    }
}
package fr.delphes.feature.streamOnline

import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class StreamOnlineFeature(
    val streamOnlineResponse: (StreamOnline) -> List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(StreamOnlineHandler())
    }

    inner class StreamOnlineHandler : EventHandler<StreamOnline> {
        override fun handle(event: StreamOnline): List<OutgoingEvent> = streamOnlineResponse(event)
    }
}
package fr.delphes.feature.streamOffline

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class StreamOfflineFeature(
    val streamOfflineResponse: (StreamOffline) -> List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(StreamOfflineHandler())
    }

    inner class StreamOfflineHandler : EventHandler<StreamOffline> {
        override fun handle(event: StreamOffline, channel: ChannelInfo): List<OutgoingEvent> = streamOfflineResponse(event)
    }
}
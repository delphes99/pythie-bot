package fr.delphes.feature.streamOffline

import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.StreamOffline
import fr.delphes.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class StreamOfflineFeature(
    val streamOfflineResponse: (StreamOffline) -> List<OutgoingEvent>
) : AbstractFeature() {
    override val streamOfflineHandlers: List<EventHandler<StreamOffline>> = listOf(StreamOfflineHandler())

    inner class StreamOfflineHandler : EventHandler<StreamOffline> {
        override fun handle(event: StreamOffline): List<OutgoingEvent> = streamOfflineResponse(event)
    }
}
package fr.delphes.feature.streamUpdate

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.StreamChanged
import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class StreamUpdate(
    private val handleChanges: (List<StreamChanges>) -> List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(Handler())
    }

    inner class Handler : EventHandler<StreamChanged> {
        override suspend fun handle(event: StreamChanged, channel: ChannelInfo): List<OutgoingEvent> {
            return handleChanges(event.changes)
        }
    }
}
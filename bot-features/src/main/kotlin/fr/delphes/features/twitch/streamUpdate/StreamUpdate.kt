package fr.delphes.features.twitch.streamUpdate

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.StreamChanged
import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature
import fr.delphes.twitch.TwitchChannel

class StreamUpdate(
    channel: TwitchChannel,
    private val handleChanges: (List<StreamChanges>) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(Handler())
    }

    inner class Handler : EventHandler<StreamChanged> {
        override suspend fun handle(event: StreamChanged, bot: ClientBot): List<OutgoingEvent> {
            return handleChanges(event.changes)
        }
    }
}
package fr.delphes.features.twitch.streamOnline

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature

class StreamOnline(
    channel: String,
    val streamOnlineResponse: (StreamOnline) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(StreamOnlineHandler())
    }

    inner class StreamOnlineHandler : EventHandler<StreamOnline> {
        override suspend fun handle(event: StreamOnline, channel: ChannelInfo): List<OutgoingEvent> = streamOnlineResponse(event)
    }
}
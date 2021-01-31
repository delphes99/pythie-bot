package fr.delphes.features.twitch.streamOffline

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature

class StreamOffline(
    channel: String,
    val streamOfflineResponse: (StreamOffline) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(StreamOfflineHandler())
    }

    inner class StreamOfflineHandler : EventHandler<StreamOffline> {
        override suspend fun handle(event: StreamOffline, channel: ChannelInfo): List<OutgoingEvent> = streamOfflineResponse(event)
    }
}
package fr.delphes.features.twitch.streamOffline

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.connector.twitch.incomingEvent.StreamOffline as StreamOfflineEvent

class StreamOffline(
    channel: TwitchChannel,
    val streamOfflineResponse: (StreamOfflineEvent) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun description() = StreamOfflineDescription(channel.name)

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(StreamOfflineHandler())
    }

    inner class StreamOfflineHandler : TwitchEventHandler<StreamOfflineEvent>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamOfflineEvent, bot: Bot): List<OutgoingEvent> = streamOfflineResponse(event)
    }
}
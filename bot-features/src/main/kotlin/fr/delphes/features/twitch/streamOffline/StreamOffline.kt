package fr.delphes.features.twitch.streamOffline

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.connector.twitch.incomingEvent.StreamOffline as StreamOfflineEvent

class StreamOffline(
    override val channel: TwitchChannel,
    val streamOfflineResponse: (StreamOfflineEvent) -> List<OutgoingEvent>
) : NonEditableFeature, TwitchFeature {
    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(StreamOfflineHandler())
        .build()

    inner class StreamOfflineHandler : TwitchEventHandler<StreamOfflineEvent>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamOfflineEvent, bot: Bot): List<OutgoingEvent> = streamOfflineResponse(event)
    }
}
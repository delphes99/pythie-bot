package fr.delphes.features.twitch.streamOnline

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.connector.twitch.incomingEvent.StreamOnline as StreamOnlineEvent

class StreamOnline(
    override val channel: TwitchChannel,
    val streamOnlineResponse: (StreamOnlineEvent) -> List<OutgoingEvent>
) : NonEditableFeature, TwitchFeature {
    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(StreamOnlineHandler())
        .build()

    inner class StreamOnlineHandler : TwitchEventHandler<StreamOnlineEvent>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamOnlineEvent, bot: Bot): List<OutgoingEvent> = streamOnlineResponse(event)
    }
}
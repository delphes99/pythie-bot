package fr.delphes.features.twitch.streamUpdate

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel

class StreamUpdate(
    override val channel: TwitchChannel,
    private val handleChanges: (List<StreamChanges>) -> List<OutgoingEvent>
) : NonEditableFeature<StreamUpdateDescription>, TwitchFeature {
    override fun description() = StreamUpdateDescription(channel.name)

    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(StreamChangedHandler())
        .build()

    inner class StreamChangedHandler : TwitchEventHandler<StreamChanged>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamChanged, bot: Bot): List<OutgoingEvent> {
            return handleChanges(event.changes)
        }
    }
}
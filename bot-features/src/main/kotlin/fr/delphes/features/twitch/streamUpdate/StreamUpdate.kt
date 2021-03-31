package fr.delphes.features.twitch.streamUpdate

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.twitch.TwitchChannel

class StreamUpdate(
    channel: TwitchChannel,
    private val handleChanges: (List<StreamChanges>) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun description() = StreamUpdateDescription(channel.name)

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(StreamChangedHandler())
    }

    inner class StreamChangedHandler : TwitchEventHandler<StreamChanged>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamChanged, bot: Bot): List<OutgoingEvent> {
            return handleChanges(event.changes)
        }
    }
}
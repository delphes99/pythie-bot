package fr.delphes.connector.twitch.feature

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.feature.featureNew.IncomingEventFilter
import fr.delphes.twitch.TwitchChannel

class ChannelFilter(
    private val channel: TwitchChannel
) : IncomingEventFilter {
    override fun isApplicable(event: IncomingEvent): Boolean {
        return event is TwitchIncomingEvent && event.isFor(channel)
    }
}
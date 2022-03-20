package fr.delphes.connector.twitch.feature

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.feature.featureNew.FeatureState
import fr.delphes.feature.featureNew.IncomingEventFilter
import fr.delphes.twitch.TwitchChannel

class ChannelFilter<STATE : FeatureState>(
    private val channel: TwitchChannel
) : IncomingEventFilter<STATE> {
    override fun isApplicable(event: IncomingEvent, state: STATE): Boolean {
        return event is TwitchIncomingEvent && event.isFor(channel)
    }
}
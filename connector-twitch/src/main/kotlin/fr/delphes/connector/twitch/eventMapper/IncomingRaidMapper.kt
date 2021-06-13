package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.IncomingRaid
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.channelRaid.IncomingRaid as TwitchIncomingRaid

class IncomingRaidMapper : TwitchIncomingEventMapper<TwitchIncomingRaid> {
    override suspend fun handle(twitchEvent: TwitchIncomingRaid): List<TwitchIncomingEvent> {
        return listOf(
            IncomingRaid(
                twitchEvent.channel,
                twitchEvent.from,
                twitchEvent.viewers
            )
        )
    }
}
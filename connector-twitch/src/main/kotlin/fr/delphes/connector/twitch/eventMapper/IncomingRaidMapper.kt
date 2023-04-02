package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.IncomingRaid
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelRaid.payload.ChannelRaidPayload
import fr.delphes.twitch.api.user.UserName

class IncomingRaidMapper : TwitchIncomingEventMapper<ChannelRaidPayload> {
    override suspend fun handle(twitchEvent: ChannelRaidPayload): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.to_broadcaster_user_name)

        return listOf(
            IncomingRaid(
                channel,
                UserName(twitchEvent.from_broadcaster_user_name),
                twitchEvent.viewers
            )
        )
    }
}
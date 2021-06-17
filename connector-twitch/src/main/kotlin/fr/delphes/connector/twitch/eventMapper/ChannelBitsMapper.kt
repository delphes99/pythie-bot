package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelCheer.payload.ChannelCheerEventPayload
import fr.delphes.twitch.api.user.User

class ChannelBitsMapper : TwitchIncomingEventMapper<ChannelCheerEventPayload> {
    override suspend fun handle(
        twitchEvent: ChannelCheerEventPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)

        return listOf(
            BitCheered(
                channel,
                twitchEvent.user_name?.let(::User),
                twitchEvent.bits,
                twitchEvent.message
            )
        )
    }
}
package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscriptionMessagePayload
import fr.delphes.twitch.api.user.User

class NewSubMessageMapper : TwitchIncomingEventMapper<ChannelSubscriptionMessagePayload> {
    override suspend fun handle(
        twitchEvent: ChannelSubscriptionMessagePayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)
        val newSub = User(twitchEvent.user_name)

        return listOf(
            NewSub(
                channel,
                newSub
            )
        )
    }
}

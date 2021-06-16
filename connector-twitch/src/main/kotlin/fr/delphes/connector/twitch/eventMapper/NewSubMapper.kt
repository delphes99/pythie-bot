package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscribeEventPayload
import fr.delphes.twitch.api.user.User

class NewSubMapper(
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<ChannelSubscribeEventPayload> {
    override suspend fun handle(
        twitchEvent: ChannelSubscribeEventPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)
        val newSub = User(twitchEvent.user_name)

        //TODO move to connector implementation
        connector.whenRunning {
            clientBot.channelOf(channel)?.state?.newSub(newSub)
        }

        return listOf(NewSub(channel, newSub))
    }
}
package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowEventPayload
import fr.delphes.twitch.api.user.User

class NewFollowMapper(
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<ChannelFollowEventPayload> {
    override suspend fun handle(
        twitchEvent: ChannelFollowEventPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)

        val incomingEvent = NewFollow(channel, User(twitchEvent.user_name))

        //TODO move to connector implementation
        connector.whenRunning {
            clientBot.channelOf(channel)?.state?.newFollow(incomingEvent.follower)
        }

        return listOf(incomingEvent)
    }
}
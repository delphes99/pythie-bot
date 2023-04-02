package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowEventPayload
import fr.delphes.twitch.api.user.UserName

class NewFollowMapper : TwitchIncomingEventMapper<ChannelFollowEventPayload> {
    override suspend fun handle(
        twitchEvent: ChannelFollowEventPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)

        return listOf(
            NewFollow(channel, UserName(twitchEvent.user_name))
        )
    }
}
package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.channelFollow.NewFollow as NewFollowTwitch

class NewFollowMapper(
    private val bot: ClientBot
) : TwitchIncomingEventMapper<NewFollowTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.channelFollow.NewFollow
    ): List<TwitchIncomingEvent> {
        val incomingEvent = NewFollow(twitchEvent.channel, twitchEvent.follower)

        bot.channelOf(twitchEvent.channel)?.state?.newFollow(incomingEvent.follower)

        return listOf(incomingEvent)
    }
}
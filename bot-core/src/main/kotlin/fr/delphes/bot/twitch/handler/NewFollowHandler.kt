package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.event.incoming.TwitchIncomingEvent
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.channelFollow.NewFollow as NewFollowTwitch

class NewFollowHandler(
    private val bot: ClientBot
) : TwitchIncomingEventHandler<NewFollowTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.channelFollow.NewFollow
    ): List<TwitchIncomingEvent> {
        val incomingEvent = NewFollow(twitchEvent.channel, twitchEvent.follower)

        bot.channelOf(twitchEvent.channel)?.state?.newFollow(incomingEvent.follower)

        return listOf(incomingEvent)
    }
}
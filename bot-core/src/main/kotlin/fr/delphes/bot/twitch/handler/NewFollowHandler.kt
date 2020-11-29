package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.newFollow.NewFollow as NewFollowTwitch

class NewFollowHandler : TwitchIncomingEventHandler<NewFollowTwitch> {
    override fun handle(
        twitchEvent: NewFollowTwitch,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        val incomingEvent = NewFollow(twitchEvent.follower)

        changeState.newFollow(incomingEvent.follower)

        return listOf(incomingEvent)
    }
}
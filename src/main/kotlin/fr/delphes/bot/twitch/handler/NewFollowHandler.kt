package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.webserver.payload.newFollow.NewFollowPayload

class NewFollowHandler : TwitchIncomingEventHandler<NewFollowPayload> {
    override fun handle(
        twitchEvent: NewFollowPayload,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        val incomingEvents = twitchEvent.data.map { d -> NewFollow(d) }
        incomingEvents.forEach { newFollow ->
            changeState.newFollow(newFollow.follower)
        }
        return incomingEvents
    }
}
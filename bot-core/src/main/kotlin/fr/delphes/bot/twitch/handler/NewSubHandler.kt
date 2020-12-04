package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.channelSubscribe.NewSub as NewSubTwitch

class NewSubHandler : TwitchIncomingEventHandler<NewSubTwitch> {
    override suspend fun handle(
        twitchEvent: NewSubTwitch,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        changeState.newSub(twitchEvent.user)

        return listOf(NewSub(twitchEvent.user))
    }
}
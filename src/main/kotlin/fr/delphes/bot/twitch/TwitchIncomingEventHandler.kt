package fr.delphes.bot.twitch

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.state.ChannelChangeState

interface TwitchIncomingEventHandler<T>  {
    fun handle(twitchEvent: T, channel: ChannelInfo, changeState: ChannelChangeState) : List<IncomingEvent>
}
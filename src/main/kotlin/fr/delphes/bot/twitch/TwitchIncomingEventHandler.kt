package fr.delphes.bot.twitch

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent

interface TwitchIncomingEventHandler<T>  {
    fun handle(twitchEvent: T, channel: ChannelInfo) : List<IncomingEvent>
}
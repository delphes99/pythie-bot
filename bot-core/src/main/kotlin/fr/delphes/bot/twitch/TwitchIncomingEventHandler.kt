package fr.delphes.bot.twitch

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.state.ChannelChangeState

interface TwitchIncomingEventHandler<T> {
    suspend fun handle(twitchEvent: T, channel: ChannelInfo, changeState: ChannelChangeState): List<IncomingEvent>
}
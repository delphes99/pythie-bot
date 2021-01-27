package fr.delphes.bot.twitch.handler

import fr.delphes.VIPParser
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.VIPListReceived
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.irc.IrcMessage

class IRCMessageHandler : TwitchIncomingEventHandler<IrcMessage> {
    override suspend fun handle(
        twitchEvent: IrcMessage,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        val vipResult = VIPParser.extractVips(twitchEvent.message)
        return if (vipResult is VIPParser.VIPResult.VIPList) {
            listOf(VIPListReceived(vipResult.users))
        } else {
            emptyList()
        }
    }
}
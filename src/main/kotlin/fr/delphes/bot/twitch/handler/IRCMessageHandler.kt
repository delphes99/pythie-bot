package fr.delphes.bot.twitch.handler

import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import fr.delphes.VIPParser
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.VIPListReceived
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class IRCMessageHandler: TwitchIncomingEventHandler<IRCMessageEvent> {
    override fun handle(twitchEvent: IRCMessageEvent, channel: ChannelInfo, changeState: ChannelChangeState): List<IncomingEvent> {
        return twitchEvent.message?.map { message ->
            val vipResult = VIPParser.extractVips(message)
            if (vipResult is VIPParser.VIPResult.VIPList) {
                listOf(VIPListReceived(vipResult.users))
            } else {
                emptyList()
            }
        }?.orElse(emptyList()) ?: emptyList()
    }
}
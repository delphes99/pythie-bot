package fr.delphes.bot.twitch.handler

import fr.delphes.VIPParser
import fr.delphes.bot.event.incoming.TwitchIncomingEvent
import fr.delphes.bot.event.incoming.VIPListReceived
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcMessage

class IRCMessageHandler(
    private val channel: TwitchChannel
) : TwitchIncomingEventHandler<IrcMessage> {
    override suspend fun handle(
        twitchEvent: IrcMessage
    ): List<TwitchIncomingEvent> {
        val vipResult = VIPParser.extractVips(twitchEvent.message)
        return if (vipResult is VIPParser.VIPResult.VIPList) {
            listOf(VIPListReceived(this@IRCMessageHandler.channel, vipResult.users))
        } else {
            emptyList()
        }
    }
}
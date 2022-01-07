package fr.delphes.connector.twitch.irc

import fr.delphes.bot.connector.ConnectorRuntime
import fr.delphes.twitch.irc.IrcClient

class TwitchIrcRuntime(
    val ircClient: IrcClient
) : ConnectorRuntime {
    override suspend fun kill() {
        ircClient.disconnect()
    }
}
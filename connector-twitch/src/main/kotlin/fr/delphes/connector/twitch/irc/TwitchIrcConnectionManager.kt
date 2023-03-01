package fr.delphes.connector.twitch.irc

import fr.delphes.bot.connector.StandAloneConnectionManager
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchConfiguration
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.outgoingEvent.TwitchChatOutgoingEvent
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

class TwitchIrcConnectionManager(
    private val connector: TwitchConnector,
) : StandAloneConnectionManager<TwitchConfiguration, TwitchIrcRuntime>(
    connector.configurationManager
) {
    override val connectionName = "Irc - Bot"

    override suspend fun doConnection(configuration: TwitchConfiguration): ConnectionSuccessful<TwitchConfiguration, TwitchIrcRuntime> {
        val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

        val ircClient = IrcClient.builder(configuration.botIdentity?.channel!!, credentialsManager).build()
        ircClient.connect()

        configuration.listenedChannels.forEach { channel ->
            ircClient.join(IrcChannel.withName(channel.channel.normalizeName))
        }

        return ConnectionSuccessful(
            configuration,
            TwitchIrcRuntime(ircClient)
        )
    }

    override suspend fun execute(event: OutgoingEvent) {
        if (event is TwitchChatOutgoingEvent) {
            val currentState = state
            if (currentState is Connected) {
                event.executeOnTwitch(currentState.runtime.ircClient, connector)
            }
        }
    }
}


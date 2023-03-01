package fr.delphes.connector.twitch.irc

import fr.delphes.bot.connector.StandAloneConnectionManager
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.connector.state.ConnectorTransition
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.ConfigurationTwitchAccount
import fr.delphes.connector.twitch.TwitchConfiguration
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.eventMapper.ChannelMessageMapper
import fr.delphes.connector.twitch.eventMapper.IRCMessageMapper
import fr.delphes.connector.twitch.outgoingEvent.TwitchOwnerChatOutgoingEvent
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import kotlinx.coroutines.runBlocking

class TwitchIrcChannelConnectionManager(
    private val channelConfiguration: ConfigurationTwitchAccount,
    private val connector: TwitchConnector,
) : StandAloneConnectionManager<TwitchConfiguration, TwitchIrcRuntime>(
    connector.configurationManager
) {
    override val connectionName = "Irc - ${channelConfiguration.channel.name}"

    override suspend fun doConnection(configuration: TwitchConfiguration): ConnectorTransition<TwitchConfiguration, TwitchIrcRuntime> {
        val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

        val ircClient = IrcClient.builder(channelConfiguration.channel, credentialsManager)
            .withOnMessage { message ->
                runBlocking {
                    IRCMessageMapper(channelConfiguration.channel).handle(message).forEach { connector.bot.handle(it) }
                }
            }
            .withOnChannelMessage { message ->
                runBlocking {
                    ChannelMessageMapper(channelConfiguration.channel, connector).handle(message).forEach { connector.bot.handle(it) }
                }
            }
            .build()

        ircClient.connect()
        ircClient.join(IrcChannel.of(channelConfiguration.channel))

        return ConnectionSuccessful(
            configuration,
            TwitchIrcRuntime(ircClient)
        )
    }

    override suspend fun execute(event: OutgoingEvent) {
        if (event is TwitchOwnerChatOutgoingEvent && event.channel == channelConfiguration.channel) {
            val currentState = state
            if (currentState is Connected) {
                event.executeOnTwitch(currentState.runtime.ircClient)
            }
        }
    }
}
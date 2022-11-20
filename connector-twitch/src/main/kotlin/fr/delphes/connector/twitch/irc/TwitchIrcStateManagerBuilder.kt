package fr.delphes.connector.twitch.irc

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.StandAloneConnectorStateMachine
import fr.delphes.bot.connector.initStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.TwitchConfiguration
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.eventMapper.ChannelMessageMapper
import fr.delphes.connector.twitch.eventMapper.IRCMessageMapper
import fr.delphes.connector.twitch.outgoingEvent.TwitchChatOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchOwnerChatOutgoingEvent
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import kotlinx.coroutines.runBlocking

object TwitchIrcStateManagerBuilder {
    fun buildBotStateManager(connector: TwitchConnector): StandAloneConnectorStateMachine<TwitchConfiguration, TwitchIrcRuntime> = initStateMachine(
        connectionName = "Irc - Bot",
        doConnection = { configuration, _ ->
            val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

            val ircClient = IrcClient.builder(configuration.botIdentity?.channel!!, credentialsManager).build()
            ircClient.connect()

            configuration.listenedChannels.forEach { channel ->
                ircClient.join(IrcChannel.withName(channel.channel.normalizeName))
            }

            ConnectionSuccessful(
                configuration,
                TwitchIrcRuntime(ircClient)
            )
        },
        executeEvent = { event ->
            if (event is TwitchChatOutgoingEvent) {
                val currentState = state
                if (currentState is Connected) {
                    event.executeOnTwitch(currentState.runtime.ircClient, connector)
                }
            }
        },
        configurationManager = connector.configurationManager
    )

    fun buildChannelStateManager(
        channelConfiguration: ChannelConfiguration,
        bot: Bot,
        connector: TwitchConnector
    ): StandAloneConnectorStateMachine<TwitchConfiguration, TwitchIrcRuntime> = initStateMachine(
        connectionName = "Irc - ${channelConfiguration.channel.name}",
        doConnection = { configuration, _ ->
            val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

            val ircClient = IrcClient.builder(channelConfiguration.channel, credentialsManager)
                .withOnMessage { message ->
                    runBlocking {
                        IRCMessageMapper(channelConfiguration.channel).handle(message).forEach { bot.handle(it) }
                    }
                }
                .withOnChannelMessage { message ->
                    runBlocking {
                        ChannelMessageMapper(channelConfiguration.channel, connector).handle(message).forEach { bot.handle(it) }
                    }
                }
                .build()

            ircClient.connect()
            ircClient.join(IrcChannel.of(channelConfiguration.channel))

            ConnectionSuccessful(
                configuration,
                TwitchIrcRuntime(ircClient)
            )
        },
        executeEvent = { event ->
            if (event is TwitchOwnerChatOutgoingEvent && event.channel == channelConfiguration.channel) {
                val currentState = state
                if (currentState is Connected) {
                    event.executeOnTwitch(currentState.runtime.ircClient)
                }
            }
        },
        configurationManager = connector.configurationManager
    )
}
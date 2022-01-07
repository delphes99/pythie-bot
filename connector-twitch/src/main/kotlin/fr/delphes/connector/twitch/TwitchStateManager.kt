package fr.delphes.connector.twitch

import fr.delphes.bot.connector.CompositeConnectorStateMachine
import fr.delphes.bot.connector.initStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.connector.twitch.irc.TwitchIrcRuntime
import fr.delphes.connector.twitch.outgoingEvent.TwitchApiOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchChatOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchOwnerChatOutgoingEvent
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import mu.KotlinLogging

class TwitchStateManager(
    private val connector: TwitchConnector
) : CompositeConnectorStateMachine<TwitchConfiguration> {
    private val legacyStateMachine = initStateMachine<TwitchConfiguration, TwitchLegacyRuntime>(
        connectionName = "Legacy",
        doConnection = { configuration, _ ->
            val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

            val clientBot = ClientBot(
                configuration,
                connector,
                connector.bot.publicUrl,
                connector.bot.configFilepath,
                connector.bot,
                credentialsManager
            )

            configuration.listenedChannels.forEach { configuredAccount ->
                val legacyChannelConfiguration = connector.channels
                    .firstOrNull { channel -> channel.channel == configuredAccount.channel }

                clientBot.register(
                    Channel(
                        configuredAccount.channel,
                        legacyChannelConfiguration,
                        credentialsManager,
                        clientBot,
                        connector
                    )
                )
            }

            clientBot.connect()

            clientBot.resetWebhook()

            ConnectionSuccessful(
                configuration,
                TwitchLegacyRuntime(
                    configuration,
                    clientBot
                )
            )
        },
        executeEvent = { event ->
            if (event is TwitchOutgoingEvent) {
                val currentState = state
                if (currentState is Connected) {
                    val clientBot = currentState.runtime.clientBot
                    try {
                        when (event) {
                            is TwitchApiOutgoingEvent -> {
                                val channel = clientBot.channelOf(event.channel)!!
                                event.executeOnTwitch(channel.twitchApi)
                            }
                            is TwitchOwnerChatOutgoingEvent -> {
                                val channel = clientBot.channelOf(event.channel)!!
                                event.executeOnTwitch(channel.ircClient)
                            }
                        }
                    } catch (e: Exception) {
                        LOGGER.error(e) { "Error while handling event ${e.message}" }
                    }
                }
            }
        },
        configurationManager = connector.configurationManager
    )

    private val ircBotStateManager = initStateMachine<TwitchConfiguration, TwitchIrcRuntime>(
        connectionName = "Irc Bot",
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
            if (event is TwitchOutgoingEvent) {
                val currentState = state
                if (currentState is Connected) {
                    try {
                        when (event) {
                            is TwitchChatOutgoingEvent -> {
                                event.executeOnTwitch(currentState.runtime.ircClient)
                            }
                        }
                    } catch (e: Exception) {
                        LOGGER.error(e) { "Error while handling event ${e.message}" }
                    }
                }
            }
        },
        configurationManager = connector.configurationManager
    )

    override val subStateManagers = listOf(
        legacyStateMachine,
        ircBotStateManager
    )


    suspend fun <T> whenRunning(
        whenRunning: suspend TwitchLegacyRuntime.() -> T,
        whenNotRunning: suspend () -> T,
    ): T {
        val currentState = legacyStateMachine.state
        return if (currentState is Connected) {
            currentState.runtime.whenRunning()
        } else {
            whenNotRunning()
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}
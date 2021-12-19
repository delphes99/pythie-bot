package fr.delphes.connector.twitch

import fr.delphes.bot.connector.CompositeConnectorStateMachine
import fr.delphes.bot.connector.initStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.connector.twitch.outgoingEvent.TwitchApiOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchChatOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchOwnerChatOutgoingEvent
import fr.delphes.twitch.auth.CredentialsManager
import mu.KotlinLogging

class TwitchStateManager(
    private val connector: TwitchConnector
) : CompositeConnectorStateMachine<TwitchConfiguration> {
    private val legacyStateMachine = initStateMachine<TwitchConfiguration, TwitchRuntime>(
        doConnection = { configuration, _ ->
            val credentialsManager = CredentialsManager(
                configuration.clientId,
                configuration.clientSecret,
                connector
            )

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
                TwitchRuntime(
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
                            is TwitchChatOutgoingEvent -> {
                                event.executeOnTwitch(clientBot.ircClient)
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

    override val subStateManagers = listOf(legacyStateMachine)


    suspend fun <T> whenRunning(
        whenRunning: suspend TwitchRuntime.() -> T,
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
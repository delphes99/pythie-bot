package fr.delphes.connector.twitch.api

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.StandAloneConnectorStateMachine
import fr.delphes.bot.connector.initStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.connector.twitch.ConfigurationTwitchAccount
import fr.delphes.connector.twitch.TwitchConfiguration
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.outgoingEvent.TwitchApiOutgoingEvent
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.ChannelTwitchClient
import kotlinx.coroutines.runBlocking
import mu.KLogger

object TwitchApiStateManagerBuilder {
    fun buildBotStateManager(connector: TwitchConnector): StandAloneConnectorStateMachine<TwitchConfiguration, TwitchApiRuntime> = initStateMachine(
        connectionName = "Helix - Application",
        doConnection = { configuration, _ ->
            val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

            val twitchApi = AppTwitchClient.build(configuration.clientId, credentialsManager)

            ConnectionSuccessful(
                configuration,
                TwitchApiRuntime(twitchApi)
            )
        },
        configurationManager = connector.configurationManager
    )

    fun buildChannelStateManager(
        channelConfiguration: ConfigurationTwitchAccount,
        bot: Bot,
        connector: TwitchConnector,
        logger: KLogger
    ): StandAloneConnectorStateMachine<TwitchConfiguration, TwitchApiChannelRuntime> = initStateMachine(
        connectionName = "Helix - ${channelConfiguration.channel.name}",
        doConnection = { configuration, _ ->
            val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

            //TODO subscribe only when feature requires
            val legacyChannelConfiguration = connector.channels
                .firstOrNull { channel -> channel.channel == channelConfiguration.channel }

            //TODO Retrieve bot api state manager ?
            val appTwitchApi = AppTwitchClient.build(configuration.clientId, credentialsManager)
            val user = runBlocking {
                appTwitchApi.getUserByName(channelConfiguration.channel.toUser())!!
            }

            //TODO random secret
            val webhookSecret = "secretWithMoreThan10caracters"

            val channelTwitchApi = ChannelTwitchClient.builder(
                appTwitchApi,
                configuration.clientId,
                credentialsManager,
                user,
                bot.publicUrl,
                bot.configFilepath,
                webhookSecret,
                legacyChannelConfiguration?.rewards ?: emptyList(),
            )
                .listenToReward()
                .listenToNewFollow()
                .listenToNewSub()
                .listenToNewCheer()
                .listenToStreamOnline()
                .listenToStreamOffline()
                .listenToChannelUpdate()
                .listenToIncomingRaid()
                .listenToPrediction()
                .listenToPoll()
                //.listenToClipCreated { clipCreatedMapper.handleTwitchEvent(it) }
                .build()

            //TODO
            //channelTwitchApi.registerWebhooks()

            ConnectionSuccessful(
                configuration,
                TwitchApiChannelRuntime(channelTwitchApi)
            )
        },
        executeEvent = { event ->
            if (event is TwitchApiOutgoingEvent && event.channel == channelConfiguration.channel) {
                val currentState = state

                try {
                    if (currentState is Connected) {
                        event.executeOnTwitch(currentState.runtime.channelTwitchApi)
                    }
                } catch (e: Exception) {
                    logger.error(e) { "Error while handling event ${e.message}" }
                }
            }
        },
        configurationManager = connector.configurationManager
    )
}
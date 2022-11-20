package fr.delphes.connector.twitch

import fr.delphes.bot.connector.StandAloneConnectorStateMachine
import fr.delphes.bot.connector.initStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.connector.twitch.outgoingEvent.TwitchApiOutgoingEvent
import mu.KLogger

object TwitchLegacyStateManagerBuilder {
    fun build(connector: TwitchConnector, logger: KLogger): StandAloneConnectorStateMachine<TwitchConfiguration, TwitchLegacyRuntime> = initStateMachine(
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
                        clientBot,
                        connector
                    )
                )
            }

            clientBot.resetWebhook()

            ConnectionSuccessful(
                configuration,
                TwitchLegacyRuntime(
                    configuration,
                    clientBot
                )
            )
        },
        configurationManager = connector.configurationManager
    )
}
package fr.delphes.connector.twitch

import fr.delphes.bot.connector.StandAloneConnectionManager
import fr.delphes.bot.connector.connectionstate.ConnectionSuccessful
import fr.delphes.bot.connector.connectionstate.ConnectorTransition
import fr.delphes.bot.event.outgoing.OutgoingEvent

class TwitchLegacyConnectionManager(
    private val connector: TwitchConnector
) : StandAloneConnectionManager<TwitchConfiguration, TwitchLegacyRuntime>(connector.configurationManager) {
    override val connectionName = "Legacy"

    override suspend fun doConnection(configuration: TwitchConfiguration): ConnectorTransition<TwitchConfiguration, TwitchLegacyRuntime> {
        val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

        val clientBot = ClientBot(
            configuration,
            connector,
            connector.botConfiguration,
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

        return ConnectionSuccessful(
            configuration,
            TwitchLegacyRuntime(
                configuration,
                clientBot
            )
        )
    }

    override suspend fun execute(event: OutgoingEvent) {
        //Nothing
    }
}
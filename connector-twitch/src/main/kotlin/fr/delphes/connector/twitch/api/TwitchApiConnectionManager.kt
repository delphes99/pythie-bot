package fr.delphes.connector.twitch.api

import fr.delphes.bot.connector.StandAloneConnectionManager
import fr.delphes.bot.connector.connectionstate.ConnectionSuccessful
import fr.delphes.bot.connector.connectionstate.ConnectorTransition
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchConfiguration
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.AppTwitchClient

class TwitchApiConnectionManager(
    private val connector: TwitchConnector,
) : StandAloneConnectionManager<TwitchConfiguration, TwitchApiRuntime>(connector.configurationManager) {
    override val connectionName = "Helix - Application"

    override suspend fun doConnection(configuration: TwitchConfiguration): ConnectorTransition<TwitchConfiguration, TwitchApiRuntime> {
        val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

        val twitchApi = AppTwitchClient.build(configuration.clientId, credentialsManager)

        return ConnectionSuccessful(
            configuration,
            TwitchApiRuntime(twitchApi)
        )
    }

    override suspend fun execute(event: OutgoingEvent) {
        //Nothing
    }
}
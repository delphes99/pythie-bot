package fr.delphes.connector.twitch.api

import fr.delphes.bot.connector.StandAloneConnectorStateMachine
import fr.delphes.bot.connector.initStateMachine
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.connector.twitch.TwitchConfiguration
import fr.delphes.connector.twitch.TwitchConnector

object TwitchApiStateManagerBuilder {
    fun buildBotStateManager(connector: TwitchConnector): StandAloneConnectorStateMachine<TwitchConfiguration, TwitchApiRuntime> = initStateMachine(
        connectionName = "Helix - Application",
        doConnection = { configuration, _ ->
            val credentialsManager = connector.configurationManager.buildCredentialsManager() ?: error("Connection with no configuration")

            ConnectionSuccessful(
                configuration,
                TwitchApiRuntime(credentialsManager, configuration)
            )
        },
        executeEvent = {

        },
        configurationManager = connector.configurationManager
    )
}
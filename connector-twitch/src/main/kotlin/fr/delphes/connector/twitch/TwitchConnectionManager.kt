package fr.delphes.connector.twitch

import fr.delphes.bot.connector.CompositeConnectionManager
import fr.delphes.bot.connector.ConnectorCommand
import fr.delphes.bot.connector.connectionstate.Connected
import fr.delphes.connector.twitch.api.TwitchApiChannelConnectionManager
import fr.delphes.connector.twitch.api.TwitchApiConnectionManager
import fr.delphes.connector.twitch.irc.TwitchIrcChannelConnectionManager
import fr.delphes.connector.twitch.irc.TwitchIrcConnectionManager
import fr.delphes.utils.addNonPresents
import fr.delphes.utils.removeAll
import mu.KotlinLogging

class TwitchConnectionManager(
    private val connector: TwitchConnector,
) : CompositeConnectionManager<TwitchConfiguration> {
    private val legacyConnectionManager = TwitchLegacyConnectionManager(connector)
    private val ircBotConnectionManager = TwitchIrcConnectionManager(connector)
    private val apiConnectionManager = TwitchApiConnectionManager(connector)

    private val ircChannelConnectionManagers = mutableMapOf<ConfigurationTwitchAccountName, TwitchIrcChannelConnectionManager>()
    private val apiChannelConnectionManagers = mutableMapOf<ConfigurationTwitchAccountName, TwitchApiChannelConnectionManager>()

    override suspend fun dispatchTransition(command: ConnectorCommand) {
        try {
            if (command == ConnectorCommand.CONNECTION_REQUESTED) {
                removeNotConfiguredConnections()
                addNotConnectedConfiguredManager()
            }
        } catch (e: Exception) {
            logger.error(e) { "Error while handling command $command" }
        }

        super.dispatchTransition(command)
    }

    private fun addNotConnectedConfiguredManager() {
        ircChannelConnectionManagers.addNonPresents(configuredChannelNames) { channelName ->
            configuredChannels.find { it.userName == channelName }?.let { channel ->
                TwitchIrcChannelConnectionManager(channel, connector)
            } ?: error("Channel $channelName not found")
        }

        apiChannelConnectionManagers.addNonPresents(configuredChannelNames) { channelName ->
            configuredChannels.find { it.userName == channelName }?.let { channel ->
                TwitchApiChannelConnectionManager(channel, connector)
            } ?: error("Channel $channelName not found")
        }
    }

    private fun removeNotConfiguredConnections() {
        ircChannelConnectionManagers.removeAll { connected ->
            configuredChannelNames
                .none { configured -> configured == connected }
        }
        apiChannelConnectionManagers.removeAll { connected ->
            configuredChannelNames
                .none { configured -> configured == connected }
        }
    }

    private val configuredChannels get() = connector.configurationManager.configuration?.listenedChannels.orEmpty()

    private val configuredChannelNames get() = configuredChannels.map { it.userName }

    override val subConnectionManagers
        get() = listOf(
            legacyConnectionManager,
            ircBotConnectionManager,
            apiConnectionManager,
            *apiChannelConnectionManagers.values.toTypedArray(),
            *ircChannelConnectionManagers.values.toTypedArray(),
        )

    //TODO migrate all call to state objects
    suspend fun <T> whenRunning(
        whenRunning: suspend TwitchLegacyRuntime.() -> T,
        whenNotRunning: suspend () -> T,
    ): T {
        val currentState = legacyConnectionManager.state
        return if (currentState is Connected) {
            currentState.runtime.whenRunning()
        } else {
            whenNotRunning()
        }
    }
}

private val logger = KotlinLogging.logger {}
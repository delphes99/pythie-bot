package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.CompositeConnectorStateMachine
import fr.delphes.bot.connector.ConfigurationManager
import fr.delphes.bot.connector.ConnectorCommand
import fr.delphes.bot.connector.StandAloneConnectorStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.status.ConnectorConnectionName
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.irc.IrcStateManagerBuilder
import fr.delphes.connector.twitch.irc.TwitchIrcRuntime
import mu.KotlinLogging

class TwitchStateManager(
    private val connector: TwitchConnector,
    private val legacyStateMachine: StandAloneConnectorStateMachine<TwitchConfiguration, TwitchLegacyRuntime>,
    private val ircBotStateManager: StandAloneConnectorStateMachine<TwitchConfiguration, TwitchIrcRuntime>,
    private val ircStateManagerBuilder: IrcStateManagerBuilder,
    private val bot: Bot,
) : CompositeConnectorStateMachine<TwitchConfiguration> {
    override suspend fun handle(command: ConnectorCommand, configurationManager: ConfigurationManager<TwitchConfiguration>) {
        try {
            if (command == ConnectorCommand.CONNECTION_REQUESTED) {
                removeUnconfiguredConnections(configurationManager)
                addConfiguredManager()
            }
        }catch (e: Exception) {
            LOGGER.error(e) { "Error while handling command $command" }
        }
        super.handle(command, configurationManager)
    }

    private fun addConfiguredManager() {
        connector.channels
            .filter(::haveNoRunningManager)
            .map { channel -> ircStateManagerBuilder.buildChannelStateManager(channel, bot, connector) }
            .let { managerToAdd -> subStateManagers.addAll(managerToAdd) }
    }

    private fun haveNoRunningManager(channel: ChannelConfiguration) = subStateManagers.map { it.connectionName }.none { it == channel.toConnectionName() }

    private suspend fun removeUnconfiguredConnections(configurationManager: ConfigurationManager<TwitchConfiguration>) {
        val connectionToDelete = subStateManagers
            .filterNot { it == legacyStateMachine }
            .filterNot { it == ircBotStateManager }
            .map { it.connectionName }.filter { connectionName -> connectionName.isNotConfigured() }
        connectionToDelete.forEach { connectionName ->
            subStateManagers
                .firstOrNull { it.connectionName == connectionName }
                ?.handle(ConnectorCommand.DISCONNECTION_REQUESTED, configurationManager)
            subStateManagers.removeIf { it.connectionName == connectionName }
        }
    }

    private fun ConnectorConnectionName.isNotConfigured() =
        connector.channels.none { channel -> channel.toConnectionName() == this }

    private fun ChannelConfiguration.toConnectionName() = "Irc - ${channel.normalizeName}"

    override val subStateManagers = mutableListOf(
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

        fun build(
            connector: TwitchConnector,
            bot: Bot,
            twitchLegacyStateManagerBuilder: TwitchLegacyStateManagerBuilder = TwitchLegacyStateManagerBuilder,
            ircStateManagerBuilder: IrcStateManagerBuilder = IrcStateManagerBuilder
        ): TwitchStateManager {
            try {
                return TwitchStateManager(
                    connector,
                    twitchLegacyStateManagerBuilder.build(connector, LOGGER),
                    ircStateManagerBuilder.buildBotStateManager(connector),
                    ircStateManagerBuilder,
                    bot
                )
            } catch (e: Exception) {
                LOGGER.error(e) { "Error while building TwitchStateManager" }
                throw e
            }
        }
    }
}
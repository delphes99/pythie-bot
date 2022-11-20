package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.CompositeConnectorStateMachine
import fr.delphes.bot.connector.ConfigurationManager
import fr.delphes.bot.connector.ConnectorCommand
import fr.delphes.bot.connector.ConnectorRuntime
import fr.delphes.bot.connector.StandAloneConnectorStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.connector.twitch.api.TwitchApiRuntime
import fr.delphes.connector.twitch.api.TwitchApiStateManagerBuilder
import fr.delphes.connector.twitch.irc.TwitchIrcRuntime
import fr.delphes.connector.twitch.irc.TwitchIrcStateManagerBuilder
import mu.KotlinLogging

class TwitchStateManager(
    private val connector: TwitchConnector,
    private val legacyStateManager: StandAloneConnectorStateMachine<TwitchConfiguration, TwitchLegacyRuntime>,
    ircBotStateManager: StandAloneConnectorStateMachine<TwitchConfiguration, TwitchIrcRuntime>,
    apiStateManager: StandAloneConnectorStateMachine<TwitchConfiguration, TwitchApiRuntime>,
    private val ircStateManagerBuilder: TwitchIrcStateManagerBuilder,
    private val apiStateManagerBuilder: TwitchApiStateManagerBuilder,
    private val bot: Bot
) : CompositeConnectorStateMachine<TwitchConfiguration> {
    override suspend fun handle(command: ConnectorCommand, configurationManager: ConfigurationManager<TwitchConfiguration>) {
        try {
            if (command == ConnectorCommand.CONNECTION_REQUESTED) {
                val specificBuilders = configurationManager.configuration?.listenedChannels?.flatMap { c -> buildersFor(c) }.orEmpty()
                removeUnconfiguredConnections(specificBuilders, configurationManager)
                addConfiguredManager(specificBuilders)
            }
        } catch (e: Exception) {
            LOGGER.error(e) { "Error while handling command $command" }
        }
        super.handle(command, configurationManager)
    }

    private fun addConfiguredManager(specificBuilders: List<ChannelSpecificBuilder>) {
        val builderToAdd = specificBuilders.filter { channelStateManagers.none { existingManager -> existingManager.connectionName == it.getConnexionName() } }

        builderToAdd.map(ChannelSpecificBuilder::build)
            .let { managerToAdd -> channelStateManagers.addAll(managerToAdd) }
    }

    private suspend fun removeUnconfiguredConnections(specificBuilders: List<ChannelSpecificBuilder>, configurationManager: ConfigurationManager<TwitchConfiguration>) {
        val specificNames = specificBuilders
            .map { it.getConnexionName() }

        val connectionToDelete = channelStateManagers
            .map { it.connectionName }
            .filter { connectionName -> specificNames.none { channelToBuild -> channelToBuild == connectionName } }

        connectionToDelete.forEach { connectionName ->
            channelStateManagers
                .firstOrNull { it.connectionName == connectionName }
                ?.handle(ConnectorCommand.DISCONNECTION_REQUESTED, configurationManager)
            channelStateManagers.removeIf { it.connectionName == connectionName }
        }
    }

    private val permanentStateManagers = listOf(
        legacyStateManager,
        ircBotStateManager,
        apiStateManager,
    )

    private val channelStateManagers = mutableListOf<StandAloneConnectorStateMachine<TwitchConfiguration, *>>()

    override val subStateManagers get() = permanentStateManagers + channelStateManagers

    suspend fun <T> whenRunning(
        whenRunning: suspend TwitchLegacyRuntime.() -> T,
        whenNotRunning: suspend () -> T,
    ): T {
        val currentState = legacyStateManager.state
        return if (currentState is Connected) {
            currentState.runtime.whenRunning()
        } else {
            whenNotRunning()
        }
    }

    class ChannelSpecificBuilder(
        val channel: ConfigurationTwitchAccount,
        val extractName: ExtractName,
        val buildFor: (ConfigurationTwitchAccount) -> StandAloneConnectorStateMachine<TwitchConfiguration, out ConnectorRuntime>
    ) {
        fun getConnexionName() = extractName(channel)

        fun build() = buildFor(channel)
    }

    private fun buildersFor(
        channel: ConfigurationTwitchAccount
    ): List<ChannelSpecificBuilder> {
        return listOf(
            ChannelSpecificBuilder(
                channel,
                { it.toIrcConnectionName() }
            ) { c -> ircStateManagerBuilder.buildChannelStateManager(c, bot, connector) },
            ChannelSpecificBuilder(
                channel,
                { it.toApiConnectionName() }
            ) { c -> apiStateManagerBuilder.buildChannelStateManager(c, bot, connector, LOGGER) }
        )
    }

    private fun ConfigurationTwitchAccount.toIrcConnectionName() = "Irc - ${channel.normalizeName}"
    private fun ConfigurationTwitchAccount.toApiConnectionName() = "Helix - ${channel.normalizeName}"

    companion object {
        private val LOGGER = KotlinLogging.logger {}

        fun build(
            connector: TwitchConnector,
            bot: Bot,
            twitchLegacyStateManagerBuilder: TwitchLegacyStateManagerBuilder = TwitchLegacyStateManagerBuilder,
            ircStateManagerBuilder: TwitchIrcStateManagerBuilder = TwitchIrcStateManagerBuilder,
            apiStateManagerBuilder: TwitchApiStateManagerBuilder = TwitchApiStateManagerBuilder,
        ): TwitchStateManager {
            try {
                return TwitchStateManager(
                    connector,
                    twitchLegacyStateManagerBuilder.build(connector, LOGGER),
                    ircStateManagerBuilder.buildBotStateManager(connector),
                    apiStateManagerBuilder.buildBotStateManager(connector),
                    ircStateManagerBuilder,
                    apiStateManagerBuilder,
                    bot
                )
            } catch (e: Exception) {
                LOGGER.error(e) { "Error while building TwitchStateManager" }
                throw e
            }
        }
    }
}


typealias ExtractName = (ConfigurationTwitchAccount) -> String
package fr.delphes.connector.twitch.api

import fr.delphes.bot.connector.StandAloneConnectionManager
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.connector.state.ConnectorTransition
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.ConfigurationTwitchAccount
import fr.delphes.connector.twitch.TwitchConfiguration
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.outgoingEvent.TwitchApiOutgoingEvent
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.ChannelTwitchClient
import kotlinx.coroutines.runBlocking
import mu.KLogger
import mu.KotlinLogging

class TwitchApiChannelConnectionManager(
    private val channelConfiguration: ConfigurationTwitchAccount,
    private val connector: TwitchConnector,
) : StandAloneConnectionManager<TwitchConfiguration, TwitchApiChannelRuntime>(connector.configurationManager) {
    override val connectionName = "Helix - ${channelConfiguration.channel.name}"

    override suspend fun doConnection(configuration: TwitchConfiguration): ConnectorTransition<TwitchConfiguration, TwitchApiChannelRuntime> {
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
            connector.bot.publicUrl,
            connector.bot.configFilepath,
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

        return ConnectionSuccessful(
            configuration,
            TwitchApiChannelRuntime(channelTwitchApi)
        )
    }

    override suspend fun execute(event: OutgoingEvent) {
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
    }
}

private val logger: KLogger = KotlinLogging.logger { }
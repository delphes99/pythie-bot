package fr.delphes.connector.twitch.state

import fr.delphes.bot.connector.ConnectorState
import fr.delphes.connector.twitch.ConfigurationTwitchAccountName
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.api.TwitchApiChannelRuntime
import fr.delphes.twitch.TwitchChannel

abstract class TwitchChannelApiConnectorState(
    val connector: TwitchConnector,
) : ConnectorState {
    suspend fun <T> whenRunning(channel: TwitchChannel, doStuff: suspend TwitchApiChannelRuntime.() -> T): T? {
        return connector.connectionManager.whenConnected(ConfigurationTwitchAccountName(channel.name)) {
            doStuff()
        }
    }
}
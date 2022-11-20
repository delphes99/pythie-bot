package fr.delphes.connector.twitch.api

import fr.delphes.bot.connector.ConnectorRuntime
import fr.delphes.connector.twitch.TwitchConfiguration
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.auth.CredentialsManager

class TwitchApiRuntime(
    credentialsManager: CredentialsManager,
    configuration: TwitchConfiguration
) : ConnectorRuntime {
    val twitchApi = AppTwitchClient.build(configuration.clientId, credentialsManager)

    override suspend fun kill() { }
}
package fr.delphes.connector.twitch.api

import fr.delphes.bot.connector.ConnectorRuntime
import fr.delphes.twitch.AppTwitchApi

class TwitchApiRuntime(
    val twitchApi: AppTwitchApi
) : ConnectorRuntime {
    override suspend fun kill() { }
}
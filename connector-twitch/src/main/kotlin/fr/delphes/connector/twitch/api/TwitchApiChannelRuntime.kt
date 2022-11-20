package fr.delphes.connector.twitch.api

import fr.delphes.bot.connector.ConnectorRuntime
import fr.delphes.twitch.ChannelTwitchClient

class TwitchApiChannelRuntime(
    val channelTwitchApi: ChannelTwitchClient
) : ConnectorRuntime {
    override suspend fun kill() {}
}
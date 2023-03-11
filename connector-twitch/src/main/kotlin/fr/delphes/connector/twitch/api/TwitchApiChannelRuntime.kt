package fr.delphes.connector.twitch.api

import fr.delphes.bot.connector.ConnectorRuntime
import fr.delphes.twitch.ChannelTwitchApi

class TwitchApiChannelRuntime(
    val channelTwitchApi: ChannelTwitchApi
) : ConnectorRuntime {
    override suspend fun kill() {}
}
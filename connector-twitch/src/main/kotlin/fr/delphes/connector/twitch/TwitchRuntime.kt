package fr.delphes.connector.twitch

import fr.delphes.bot.connector.ConnectorRuntime

class TwitchRuntime(
    val configuration: TwitchConfiguration,
    val clientBot: ClientBot
): ConnectorRuntime {
    override suspend fun kill() {
        //TODO
    }
}

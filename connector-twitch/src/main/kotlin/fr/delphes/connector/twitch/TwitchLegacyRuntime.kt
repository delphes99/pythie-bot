package fr.delphes.connector.twitch

import fr.delphes.bot.connector.ConnectorRuntime

class TwitchLegacyRuntime(
    val configuration: TwitchConfiguration,
    val clientBot: ClientBot
): ConnectorRuntime {
    override suspend fun kill() {
        //TODO
    }
}

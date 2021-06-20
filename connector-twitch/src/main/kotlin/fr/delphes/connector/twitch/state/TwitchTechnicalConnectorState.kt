package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.TwitchChannel

interface BotAccountProvider {
    val botAccount: TwitchChannel?
}

class TwitchTechnicalConnectorState(
    private val twitchConnector: TwitchConnector
) : BotAccountProvider {
    override val botAccount get() = twitchConnector.botAccount
}

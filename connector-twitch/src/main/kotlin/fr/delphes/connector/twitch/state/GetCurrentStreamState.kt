package fr.delphes.connector.twitch.state

import fr.delphes.bot.connector.ConnectorState
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.state.StateId
import fr.delphes.twitch.TwitchChannel

class GetCurrentStreamState(
    private val twitchConnector: TwitchConnector,
) : ConnectorState {
    override val id = ID

    fun getStreamInfosOf(channel: TwitchChannel) =
        twitchConnector.statistics.of(channel).currentStream

    companion object {
        val ID = StateId.from<GetCurrentStreamState>("twitch-connector-get-stream-infos")
    }
}
package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.state.StateId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.TwitchUser

class GetVipState(connector: TwitchConnector) : TwitchChannelApiConnectorState(connector) {
    override val id = ID

    suspend fun getVipOf(channel: TwitchChannel): List<TwitchUser> {
        return whenRunning(channel) {
            this.channelTwitchApi.getVIPs()
        } ?: emptyList()
    }

    companion object {
        val ID = StateId.from<GetVipState>("twitch-connector-get-vip")
    }
}
package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName

data class RemoveModerator(
    val user: UserName,
    override val channel: TwitchChannel
) : TwitchApiOutgoingEvent {
    override suspend fun executeOnTwitch(twitchApi: ChannelTwitchApi, connector: TwitchConnector) {
        connector.getUser(user)?.id?.also { id ->
            twitchApi.removeModerator(id)
        }
    }
}
package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName

data class PromoteVIP(
    val user: UserName,
    override val channel: TwitchChannel
) : TwitchApiOutgoingEvent {
    constructor(
        user: String,
        channel: TwitchChannel
    ) : this(UserName(user), channel)

    override suspend fun executeOnTwitch(twitchApi: ChannelTwitchApi, connector: TwitchConnector) {
        connector.getUser(user)?.id?.also { id ->
            twitchApi.promoteVip(id)
        }
    }
}
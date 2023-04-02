package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.api.user.User

data class RemoveVIP(
    val user: User,
    override val channel: TwitchChannel
) : TwitchApiOutgoingEvent {
    constructor(
        user: String,
        channel: TwitchChannel
    ) : this(User(user), channel)

    override suspend fun executeOnTwitch(twitchApi: ChannelTwitchApi, connector: TwitchConnector) {
        connector.getUser(user)?.toTwitchUser()?.let { user ->
            twitchApi.removeVip(user)
        }
    }

    private fun UserInfos.toTwitchUser(): TwitchUser {
        return TwitchUser(
            id = id,
            name = name,
            broadcasterType = broadcasterType,
            viewCount = viewCount
        )
    }
}
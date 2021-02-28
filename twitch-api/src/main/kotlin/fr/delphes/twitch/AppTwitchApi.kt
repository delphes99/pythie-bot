package fr.delphes.twitch

import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.api.video.ChannelVideo
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload

interface AppTwitchApi {
    suspend fun getAllSubscriptions(): ListSubscriptionsPayload

    suspend fun removeAllSubscriptions()

    suspend fun getUserByName(user: User): TwitchUser?

    suspend fun getVideosOf(channelId: String, limit: Int): List<ChannelVideo>
}
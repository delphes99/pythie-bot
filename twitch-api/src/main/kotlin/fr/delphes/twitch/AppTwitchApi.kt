package fr.delphes.twitch

import fr.delphes.twitch.api.channel.ChannelInformation
import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.api.user.UserName
import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.api.video.ChannelVideo
import fr.delphes.twitch.api.video.payload.ChannelVideoType
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload

interface AppTwitchApi {
    suspend fun getAllSubscriptions(): ListSubscriptionsPayload

    suspend fun removeAllSubscriptions()

    suspend fun getUserByName(userName: UserName): TwitchUser?

    suspend fun getUserById(userId: UserId): TwitchUser?

    suspend fun getChannelInformation(userId: UserId): ChannelInformation?

    suspend fun getVideosOf(userId: UserId, channelVideoType: ChannelVideoType): List<ChannelVideo>
}
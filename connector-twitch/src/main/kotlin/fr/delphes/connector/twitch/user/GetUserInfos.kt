package fr.delphes.connector.twitch.user

import fr.delphes.twitch.AppTwitchApi
import fr.delphes.twitch.api.user.UserName
import fr.delphes.twitch.api.video.payload.ChannelVideoType

suspend fun getUserInfos(user: UserName, twitchApi: AppTwitchApi): UserInfos? {
    return twitchApi.getUserByName(user)?.let { twitchUserInfos ->
        val videos = twitchApi.getVideosOf(twitchUserInfos.id, ChannelVideoType.all)

        val lastVideo = videos.maxByOrNull { video -> video.createdAt }
        UserInfos(
            name = user.name,
            id = twitchUserInfos.id,
            broadcasterType = twitchUserInfos.broadcasterType,
            viewCount = twitchUserInfos.viewCount,
            lastStreamTitle = lastVideo?.title,
            lastStreamDate = lastVideo?.createdAt
        )
    }
}
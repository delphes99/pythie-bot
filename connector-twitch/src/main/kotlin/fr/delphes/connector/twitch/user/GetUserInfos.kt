package fr.delphes.connector.twitch.user

import fr.delphes.twitch.AppTwitchApi
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.api.user.payload.BroadcasterType
import fr.delphes.twitch.api.video.payload.ChannelVideoType
import mu.KotlinLogging

private val LOGGER = KotlinLogging.logger {}

suspend fun getUserInfos(user: User, twitchApi: AppTwitchApi): UserInfos {
    return twitchApi.getUserByName(user)?.let { twitchUserInfos ->
        val videos = twitchApi.getVideosOf(twitchUserInfos.id, ChannelVideoType.all)

        val lastVideo = videos.maxByOrNull { video -> video.createdAt }
        UserInfos(
            name = user.name,
            broadcasterType = twitchUserInfos.broadcasterType,
            viewCount = twitchUserInfos.viewCount,
            lastStreamTitle = lastVideo?.title,
            lastStreamDate = lastVideo?.createdAt
        )
    } ?: run {
        LOGGER.error { "try to get non existing user" }
        UserInfos(user.name, BroadcasterType.DEFAULT, 0)
    }
}
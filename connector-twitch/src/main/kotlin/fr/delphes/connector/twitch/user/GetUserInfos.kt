package fr.delphes.connector.twitch.user

import fr.delphes.twitch.AppTwitchApi
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.api.user.payload.BroadcasterType
import fr.delphes.twitch.api.video.ChannelVideo
import mu.KotlinLogging

private val LOGGER = KotlinLogging.logger {}

suspend fun getUserInfos(user: User, twitchApi: AppTwitchApi): UserInfos {
    return twitchApi.getUserByName(user)?.
    let { twitchUserInfos ->
        val categories = twitchApi.getVideosOf(twitchUserInfos.id, 3).map(ChannelVideo::game).distinct()

        UserInfos(user.name, twitchUserInfos.broadcasterType, twitchUserInfos.viewCount, categories)
    } ?: run {
        LOGGER.error { "try to get non existing user" }
        UserInfos(user.name, BroadcasterType.DEFAULT, 0, emptyList())
    }
}
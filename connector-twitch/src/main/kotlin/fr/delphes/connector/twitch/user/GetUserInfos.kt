package fr.delphes.connector.twitch.user

import fr.delphes.twitch.AppTwitchApi
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.api.user.payload.BroadcasterType
import fr.delphes.twitch.api.video.ChannelVideo
import mu.KotlinLogging
import java.time.LocalDateTime

private val LOGGER = KotlinLogging.logger {}

suspend fun getUserInfos(user: User, twitchApi: AppTwitchApi, now: LocalDateTime): UserInfos {
    return twitchApi.getUserByName(user)?.
    let { twitchUserInfos ->
        val categories = twitchApi.getVideosOf(twitchUserInfos.id, 3).map(ChannelVideo::game).distinct()

        UserInfos(user.name, now, twitchUserInfos.broadcasterType, twitchUserInfos.viewCount, categories)
    } ?: run {
        LOGGER.error { "try to get non existing user" }
        UserInfos(user.name, now, BroadcasterType.DEFAULT, 0, emptyList())
    }
}
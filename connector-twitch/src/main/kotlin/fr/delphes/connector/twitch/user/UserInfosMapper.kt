package fr.delphes.connector.twitch.user

import fr.delphes.twitch.api.user.TwitchUser


internal fun UserInfos.toTwitchUser(): TwitchUser {
    return TwitchUser(
        id = id,
        name = name,
        broadcasterType = broadcasterType,
        viewCount = viewCount
    )
}
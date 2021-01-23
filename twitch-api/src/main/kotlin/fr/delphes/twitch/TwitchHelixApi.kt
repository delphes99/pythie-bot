package fr.delphes.twitch

import fr.delphes.twitch.api.user.payload.UserInfosForToken
import fr.delphes.twitch.auth.AuthToken

interface TwitchHelixApi {
    suspend fun getUserInfosOf(authToken: AuthToken): UserInfosForToken
}
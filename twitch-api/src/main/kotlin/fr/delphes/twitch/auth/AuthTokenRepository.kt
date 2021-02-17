package fr.delphes.twitch.auth

import fr.delphes.twitch.TwitchChannel

interface AuthTokenRepository {
    fun getAppToken(): AuthToken?

    suspend fun newAppToken(token: AuthToken)

    fun getChannelToken(channel: TwitchChannel): AuthToken?

    suspend fun newChannelToken(channel: TwitchChannel, newToken: AuthToken)
}
package fr.delphes.twitch.auth

import fr.delphes.twitch.TwitchChannel

interface AuthTokenRepository {
    fun getAppToken(): AuthToken?

    fun newAppToken(token: AuthToken)

    fun getChannelToken(channel: TwitchChannel): AuthToken?

    fun newChannelToken(channel: TwitchChannel, newToken: AuthToken)
}
package fr.delphes.twitch.auth

interface AuthApi {
    suspend fun refreshToken(oldToken: AuthToken, appCredential: TwitchAppCredential): AuthToken

    suspend fun getAppToken(clientId: String, clientSecret: String): AuthToken
}
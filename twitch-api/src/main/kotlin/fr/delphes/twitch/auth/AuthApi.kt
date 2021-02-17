package fr.delphes.twitch.auth

interface AuthApi {
    suspend fun refreshToken(oldToken: AuthToken, clientId: String, clientSecret: String): AuthToken

    suspend fun getAppToken(clientId: String, clientSecret: String): AuthToken
}
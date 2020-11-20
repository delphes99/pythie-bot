package fr.delphes.twitch.auth

interface WithAuthToken {
    var authToken: AuthToken?

    suspend fun refreshToken()
}
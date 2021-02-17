package fr.delphes.twitch.auth

import fr.delphes.twitch.TwitchChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CredentialsManager(
    private val clientId: String,
    private val clientSecret: String,
    private val authTokenRepository: AuthTokenRepository,

) {
    private val lock = Mutex()
    private val authApi: AuthApi = AuthClient()

    suspend fun getAppToken(): AuthToken {
        lock.withLock {
            return authTokenRepository.getAppToken()
                ?: getAndSaveAppToken()
        }
    }

    private suspend fun getAndSaveAppToken(): AuthToken {
        return authApi.getAppToken(clientId, clientSecret).also {
            authTokenRepository.newAppToken(it)
        }
    }

    suspend fun getAppTokenRefreshed(): AuthToken {
        lock.withLock {
            val token = authApi.getAppToken(clientId, clientSecret)

            authTokenRepository.newAppToken(token)

            return token
        }
    }

    suspend fun getChannelToken(channel: TwitchChannel): AuthToken? {
        lock.withLock {
            return authTokenRepository.getChannelToken(channel)
        }
    }

    suspend fun getChannelTokenRefreshed(channel: TwitchChannel): AuthToken {
        lock.withLock {
            val currentToken = authTokenRepository.getChannelToken(channel)

            if(currentToken == null) {
                error("Token must be provided for channel ${channel.name}")
            } else {
                val newToken = authApi.refreshToken(currentToken, clientId, clientSecret)

                authTokenRepository.newChannelToken(channel, newToken)

                return newToken
            }
        }
    }
}

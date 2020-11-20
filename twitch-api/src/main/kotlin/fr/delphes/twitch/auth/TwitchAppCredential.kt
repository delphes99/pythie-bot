package fr.delphes.twitch.auth

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import mu.KotlinLogging

data class TwitchAppCredential(
    val clientId: String,
    val clientSecret: String,
    val authApi: AuthApi,
    val tokenRepository: AuthTokenRepository,
    override var authToken: AuthToken? = null
): WithAuthToken {
    private val lock = Mutex()

    override suspend fun refreshToken() {
        lock.withLock {
            val oldToken = authToken
            if (oldToken != null) {
                val newToken = authApi.refreshToken(oldToken, this)
                authToken = newToken
                tokenRepository.save(newToken)
            } else {
                LOGGER.error { "No token to refresh" }
            }
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}

        fun of(
            clientId: String,
            clientSecret: String,
            tokenRepository: (suspend () -> AuthToken) -> AuthTokenRepository
        ): TwitchAppCredential {
            val authApi = AuthClient()
            val repository = tokenRepository {
                authApi.getAppToken(clientId, clientSecret)
            }
            val token = runBlocking {
                val token = repository.load()
                repository.save(token)
                token
            }

            return TwitchAppCredential(
                clientId,
                clientSecret,
                authApi,
                repository,
                token
            )
        }
    }
}
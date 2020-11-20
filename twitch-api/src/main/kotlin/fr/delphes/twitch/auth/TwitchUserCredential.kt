package fr.delphes.twitch.auth

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import mu.KotlinLogging

class TwitchUserCredential(
    private val appCredential: TwitchAppCredential,
    private val authApi: AuthApi,
    private val tokenRepository: AuthTokenRepository,
    override var authToken: AuthToken? = null
): WithAuthToken {
    private val lock = Mutex()

    override suspend fun refreshToken() {
        lock.withLock {
            val oldToken = authToken
            if (oldToken != null) {
                val newToken = authApi.refreshToken(oldToken, appCredential)
                authToken = newToken
                tokenRepository.save(newToken)
            } else {
                LOGGER.error { "No token to refresh" }
            }
        }
    }

    suspend fun newAuth(auth: AuthToken) {
        lock.withLock {
            authToken = auth
            tokenRepository.save(auth)
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}

        fun of(
            appCredential: TwitchAppCredential,
            tokenRepository: AuthTokenRepository
        ): TwitchUserCredential {
            val authToken = runBlocking {
                tokenRepository.load()
            }
            return TwitchUserCredential(
                appCredential,
                AuthClient(),
                tokenRepository,
                authToken
            )
        }
    }
}

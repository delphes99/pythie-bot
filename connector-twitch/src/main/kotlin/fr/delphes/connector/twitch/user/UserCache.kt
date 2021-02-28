package fr.delphes.connector.twitch.user

import fr.delphes.twitch.api.user.User
import fr.delphes.utils.time.Clock
import java.time.Duration
import java.time.LocalDateTime

class UserCache(
    private val expirationDuration: Duration,
    private val clock: Clock,
    private val cache: MutableMap<User, UserInfos> = mutableMapOf(),
    private val retrieveUser: suspend (User, LocalDateTime) -> UserInfos,
) {
    constructor(
        retrieveUser: suspend (User, LocalDateTime) -> UserInfos,
        expirationDuration: Duration,
        clock: Clock,
        vararg cachedValues: Pair<User, UserInfos>,
    ) : this(expirationDuration, clock, mutableMapOf(*cachedValues), retrieveUser)

    suspend fun getUser(twitchUser: User): UserInfos {
        val cacheValue = cache[twitchUser]

        val now = clock.now()
        return if (cacheValue == null || cacheValue.since.plus(expirationDuration).isBefore(now)) {
            val newValue = retrieveUser(twitchUser, now)
            cache[twitchUser] = newValue
            newValue
        } else {
            cacheValue
        }
    }
}
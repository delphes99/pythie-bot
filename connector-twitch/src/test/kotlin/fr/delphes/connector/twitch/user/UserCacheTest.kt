package fr.delphes.connector.twitch.user

import fr.delphes.connector.twitch.TestClock
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.api.user.payload.BroadcasterType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

internal class UserCacheTest {
    private val NOW = LocalDateTime.of(2021, 1, 1, 1, 1)
    private val CACHED_TIME = NOW.minusMinutes(5)
    private val CACHED_TIME_EXPIRED = NOW.minusMinutes(30)
    private val USER_NAME = "user"
    private val USER = User(USER_NAME)
    private val CATEGORIES = listOf("game")

    private val clock = TestClock(NOW)

    @Test
    internal fun `get user infos when not cached`() {
        val emptyCache = UserCache(
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
        ) { _, now -> UserInfos(USER_NAME, now, BroadcasterType.AFFILIATE, 50, CATEGORIES) }

        val userInfos = runBlocking {
            emptyCache.getUser(USER)
        }

        assertThat(userInfos).isEqualTo(UserInfos(USER_NAME, NOW, BroadcasterType.AFFILIATE, 50, CATEGORIES))
    }

    @Test
    internal fun `cached value`() {
        val cache = UserCache(
            retrieveUser = { _, now -> UserInfos(USER_NAME, now, BroadcasterType.AFFILIATE, 50, CATEGORIES) },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
            USER to UserInfos(USER_NAME, CACHED_TIME, BroadcasterType.AFFILIATE, 50, CATEGORIES),
        )

        val userInfos = runBlocking {
            cache.getUser(USER)
        }

        assertThat(userInfos).isEqualTo(UserInfos(USER_NAME, CACHED_TIME, BroadcasterType.AFFILIATE, 50, CATEGORIES))
    }

    @Test
    internal fun `retrieve fresh infos if cached values is too old`() {
        val cache = UserCache(
            retrieveUser = { _, now -> UserInfos(USER_NAME, now, BroadcasterType.AFFILIATE, 50, CATEGORIES) },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
            USER to UserInfos(USER_NAME, CACHED_TIME_EXPIRED, BroadcasterType.AFFILIATE, 50, CATEGORIES),
        )

        val userInfos = runBlocking {
            cache.getUser(USER)
        }

        assertThat(userInfos).isEqualTo(UserInfos(USER_NAME, NOW, BroadcasterType.AFFILIATE, 50, CATEGORIES))
    }
}
package fr.delphes.utils.cache

import fr.delphes.utils.time.TestClock
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class InMemoryCacheTest {
    companion object {
        private val NOW = LocalDateTime.of(2021, 1, 1, 1, 1)
        private val CACHED_TIME = NOW.minusMinutes(5)
        private val CACHED_TIME_EXPIRED = NOW.minusMinutes(30)
        private const val KEY = "key"
        private const val CACHED_VALUE = "value"
        private const val NEW_VALUE = "newValue"
        private val clock = TestClock(NOW)
    }

    @Test
    internal fun `retrieve infos when not cached`() {
        val emptyCache = InMemoryCache<String, String>(
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
        ) { NEW_VALUE }

        val retrieveValue = runBlocking {
            emptyCache.getValue(KEY)
        }

        retrieveValue shouldBe NEW_VALUE
    }

    @Test
    internal fun `get cached value`() {
        val cache = InMemoryCache(
            retrieve = { NEW_VALUE },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
            KEY to CacheValue(CACHED_VALUE, CACHED_TIME),
        )

        val retrieveValue = runBlocking {
            cache.getValue(KEY)
        }

        retrieveValue shouldBe CACHED_VALUE
    }

    @Test
    internal fun `retrieve fresh infos if cached values is too old`() {
        val cache = InMemoryCache(
            retrieve = { NEW_VALUE },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
            KEY to CacheValue(CACHED_VALUE, CACHED_TIME_EXPIRED),
        )

        val retrieveValue = runBlocking {
            cache.getValue(KEY)
        }

        retrieveValue shouldBe NEW_VALUE
    }

    @Test
    internal fun `retrieve no infos if unable to retrieve infos`() {
        val cache = InMemoryCache<String, String>(
            retrieve = { null },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock
        )

        val retrieveValue = runBlocking {
            cache.getValue(KEY)
        }

        retrieveValue.shouldBeNull()
    }

    @Test
    internal fun `retrieve old data if unable to retrieve infos`() {
        val cache = InMemoryCache(
            retrieve = { null },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
            KEY to CacheValue(CACHED_VALUE, CACHED_TIME_EXPIRED),
        )

        val retrieveValue = runBlocking {
            cache.getValue(KEY)
        }

        retrieveValue shouldBe CACHED_VALUE
    }
}
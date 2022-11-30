package fr.delphes.utils.cache

import fr.delphes.utils.time.TestClock
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDateTime

class InMemoryCacheTest : ShouldSpec({
    should("retrieve infos when not cached") {
        val emptyCache = InMemoryCache<String, String>(
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
        ) { NEW_VALUE }

        emptyCache.getValue(KEY) shouldBe NEW_VALUE
    }

    should("get cached value") {
        val cache = InMemoryCache(
            retrieve = { NEW_VALUE },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
            KEY to CacheValue(CACHED_VALUE, CACHED_TIME),
        )

        cache.getValue(KEY) shouldBe CACHED_VALUE
    }

    should("retrieve fresh infos if cached values is too old") {
        val cache = InMemoryCache(
            retrieve = { NEW_VALUE },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
            KEY to CacheValue(CACHED_VALUE, CACHED_TIME_EXPIRED),
        )

        cache.getValue(KEY) shouldBe NEW_VALUE
    }

    should("retrieve no infos if unable to retrieve infos") {
        val cache = InMemoryCache<String, String>(
            retrieve = { null },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock
        )

        cache.getValue(KEY).shouldBeNull()
    }

    should("retrieve old data if unable to retrieve infos") {
        val cache = InMemoryCache(
            retrieve = { null },
            expirationDuration = Duration.ofMinutes(10),
            clock = clock,
            KEY to CacheValue(CACHED_VALUE, CACHED_TIME_EXPIRED),
        )

        cache.getValue(KEY) shouldBe CACHED_VALUE
    }
}) {
    companion object {
        private val NOW = LocalDateTime.of(2021, 1, 1, 1, 1)
        private val CACHED_TIME = NOW.minusMinutes(5)
        private val CACHED_TIME_EXPIRED = NOW.minusMinutes(30)
        private const val KEY = "key"
        private const val CACHED_VALUE = "value"
        private const val NEW_VALUE = "newValue"
        private val clock = TestClock(NOW)
    }
}
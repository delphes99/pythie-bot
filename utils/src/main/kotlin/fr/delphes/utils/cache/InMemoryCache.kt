package fr.delphes.utils.cache

import fr.delphes.utils.time.Clock
import java.time.Duration

class InMemoryCache<KEY, VALUE>(
    private val expirationDuration: Duration,
    private val clock: Clock,
    private val cache: MutableMap<KEY, CacheValue<VALUE>> = mutableMapOf(),
    private val retrieve: suspend (KEY) -> VALUE?,
) {
    constructor(
        retrieve: suspend (KEY) -> VALUE?,
        expirationDuration: Duration,
        clock: Clock,
        vararg cachedValues: Pair<KEY, CacheValue<VALUE>>,
    ) : this(
        expirationDuration,
        clock,
        cachedValues.toMap().toMutableMap(),
        retrieve
    )

    suspend fun getValue(key: KEY): VALUE? {
        val cacheValue = cache[key]

        val now = clock.now()
        return if (cacheValue == null || cacheValue.cacheInsertTime.plus(expirationDuration).isBefore(now)) {
            retrieve(key)?.also { retrievedValue ->
                cache[key] = CacheValue(retrievedValue, now)
            } ?: cacheValue?.value
        } else {
            cacheValue.value
        }
    }
}
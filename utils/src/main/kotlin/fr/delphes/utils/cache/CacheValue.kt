package fr.delphes.utils.cache

import java.time.LocalDateTime

data class CacheValue<T>(
    val value: T?,
    val cacheInsertTime: LocalDateTime
)

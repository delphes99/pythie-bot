@file:UseSerializers(LocalDateTimeSerializer::class, DurationSerializer::class)

package fr.delphes.feature.voth

import fr.delphes.User
import fr.delphes.event.incoming.RewardCost
import fr.delphes.storage.serialization.DurationSerializer
import fr.delphes.storage.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration
import java.time.LocalDateTime

@Serializable
data class VOTHWinner(
    val user: User,
    val since: LocalDateTime?,
    val cost: RewardCost,
    val previousPeriods: List<Duration> = emptyList()
) {
    constructor(
        user: String,
        since: LocalDateTime?,
        cost: RewardCost,
        previousPeriods: List<Duration> = emptyList()
    ) : this(User(user), since, cost, previousPeriods)

    fun duration(now: LocalDateTime): Duration {
        return (previousPeriods.reduceOrNull(Duration::plus) ?: Duration.ZERO) +
                (since?.let { Duration.between(since, now) } ?: Duration.ZERO)
    }
}
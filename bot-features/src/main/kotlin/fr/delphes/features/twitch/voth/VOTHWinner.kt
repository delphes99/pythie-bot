@file:UseSerializers(LocalDateTimeSerializer::class, DurationSerializer::class)

package fr.delphes.features.twitch.voth

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardCost
import fr.delphes.twitch.api.user.UserName
import fr.delphes.utils.serialization.LocalDateTimeSerializer
import fr.delphes.utils.serialization.DurationSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration
import java.time.LocalDateTime

@Serializable
data class VOTHWinner(
    val user: UserName,
    val since: LocalDateTime?,
    val cost: RewardCost,
    val previousPeriods: List<Duration> = emptyList()
) {
    constructor(
        user: String,
        since: LocalDateTime?,
        cost: RewardCost,
        previousPeriods: List<Duration> = emptyList()
    ) : this(UserName(user), since, cost, previousPeriods)

    fun duration(now: LocalDateTime): Duration {
        return (previousPeriods.reduceOrNull(Duration::plus) ?: Duration.ZERO) +
                (since?.let { Duration.between(since, now) } ?: Duration.ZERO)
    }
}
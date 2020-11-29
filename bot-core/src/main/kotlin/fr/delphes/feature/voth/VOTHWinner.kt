@file:UseSerializers(LocalDateTimeSerializer::class, DurationSerializer::class)

package fr.delphes.feature.voth

import fr.delphes.twitch.api.user.User
import fr.delphes.bot.util.serialization.DurationSerializer
import fr.delphes.bot.util.serialization.LocalDateTimeSerializer
import fr.delphes.twitch.api.channel.channelPointsCustomRewardRedemption.RewardCost
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
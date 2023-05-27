package fr.delphes.features.twitch.voth

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.feature.State
import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDateTime

@Serializable
data class VOTHState(
    val currentVip: VOTHWinner? = null,
    val previousReigns: List<VOTHReign> = listOf(),
) : State {
    fun newVOTH(newVOTH: RewardRedemption, now: LocalDateTime): VOTHState {
        val currentVip = this.currentVip
        val reigns = if (currentVip != null) {
            previousReigns + VOTHReign(currentVip.user, currentVip.duration(now), currentVip.cost)
        } else {
            previousReigns
        }


        return VOTHState(VOTHWinner(newVOTH.user, now, newVOTH.cost), reigns)
    }

    fun pause(now: LocalDateTime): VOTHState {
        return if (currentVip?.since != null) {
            val currentPeriod = Duration.between(currentVip.since, now)
            copy(
                currentVip = currentVip.copy(
                    since = null,
                    previousPeriods = currentVip.previousPeriods.plus(currentPeriod)
                )
            )
        } else {
            this
        }
    }

    fun unpause(now: LocalDateTime): VOTHState {
        return copy(
            currentVip = currentVip?.copy(since = currentVip.since ?: now)
        )
    }

    fun getReignsFor(user: UserName, now: LocalDateTime): Stats {
        val previousReigns = previousReigns.filter { reign -> reign.voth == user }

        val reigns = if (currentVip?.user == user) {
            val currentReign = VOTHReign(user, currentVip.duration(now), currentVip.cost)
            previousReigns + currentReign
        } else {
            previousReigns
        }

        return Stats(user, reigns)
    }

    fun top3(now: LocalDateTime): List<Stats> {
        return allWinners().map { getReignsFor(it, now) }.sortedByDescending { it.totalTime }.take(3)
    }

    private fun allWinners(): List<UserName> {
        return previousReigns.map { reign -> reign.voth }.plus(currentVip?.user).filterNotNull().distinct()
    }

    fun lastReigns(now: LocalDateTime): List<VOTHReign> {
        val currentVip = currentVip

        return if (currentVip != null) {
            val currentReign = VOTHReign(currentVip.user, currentVip.duration(now), currentVip.cost)
            previousReigns + currentReign
        } else {
            previousReigns
        }.reversed()
    }
}

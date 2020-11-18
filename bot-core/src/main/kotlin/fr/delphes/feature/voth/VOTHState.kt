package fr.delphes.feature.voth

import fr.delphes.twitch.model.User
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.feature.State
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDateTime

@Serializable
class VOTHState(
    var vothChanged: Boolean = false,
    var currentVip: VOTHWinner? = null,
    val previousReigns: MutableList<VOTHReign> = mutableListOf()
) : State {
    fun newVOTH(newVOTH: RewardRedemption, now: LocalDateTime): VOTHWinner {
        val currentVip = this.currentVip
        if (currentVip != null) {
            val reign = VOTHReign(currentVip.user, currentVip.duration(now), currentVip.cost)
            previousReigns.add(reign)
        }

        val vothWinner = VOTHWinner(newVOTH.user, now, newVOTH.cost)

        vothChanged = true
        this.currentVip = vothWinner

        return vothWinner
    }

    fun pause(now: LocalDateTime) {
        val currentVip = currentVip
        if (currentVip != null) {
            val currentPeriod = Duration.between(currentVip.since, now)
            this.currentVip = currentVip.copy(
                since = null,
                previousPeriods = currentVip.previousPeriods.plus(currentPeriod)
            )
        }
    }

    fun unpause(now: LocalDateTime) {
        this.currentVip = this.currentVip?.copy(since = now)
    }

    fun getReignsFor(user: User, now: LocalDateTime): Stats {
        val currentVip = currentVip
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

    private fun allWinners(): List<User> {
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

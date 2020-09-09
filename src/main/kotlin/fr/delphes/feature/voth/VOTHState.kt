package fr.delphes.feature.voth

import fr.delphes.User
import fr.delphes.event.incoming.RewardRedemption
import java.time.LocalDateTime

class VOTHState(
    var vothChanged: Boolean = false,
    var currentVip: VOTHWinner? = null,
    val previousReigns: MutableList<VOTHReign> = mutableListOf()
) {

    fun newVOTH(newVOTH: RewardRedemption, now: LocalDateTime): VOTHWinner {
        val currentVip = this.currentVip
        if(currentVip != null) {
            val reign = VOTHReign(currentVip.user, currentVip.since, now, currentVip.cost)
            previousReigns.add(reign)
        }

        val vothWinner = VOTHWinner(newVOTH.user, now, newVOTH.cost)

        vothChanged = true
        this.currentVip = vothWinner

        return vothWinner
    }

    fun getReignsFor(user: User, now: LocalDateTime): Stats {
        val currentVip = currentVip
        val previousReigns = previousReigns.filter { reign -> reign.voth == user }

        val reigns = if (currentVip?.user == user) {
            val currentReign = VOTHReign(user, currentVip.since, now, currentVip.cost)
            previousReigns + currentReign
        } else {
            previousReigns
        }

        return Stats(reigns)
    }
}

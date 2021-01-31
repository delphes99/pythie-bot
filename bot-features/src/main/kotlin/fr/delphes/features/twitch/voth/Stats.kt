package fr.delphes.features.twitch.voth

import fr.delphes.twitch.api.user.User
import java.time.Duration

data class Stats(
    val user: User,
    private val reigns: List<VOTHReign>
) {
    constructor(user: User, vararg reigns: VOTHReign) : this(user, listOf(*reigns))

    val totalTime = reigns.map(VOTHReign::duration).fold(Duration.ZERO) { acc, duration -> acc.plus(duration) }
    val totalCost = reigns.map(VOTHReign::cost).fold(0L) { acc, cost -> acc + cost }
    val numberOfReigns = reigns.count()
}
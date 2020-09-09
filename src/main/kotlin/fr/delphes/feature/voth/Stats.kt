package fr.delphes.feature.voth

import java.time.Duration

data class Stats(
    private val reigns: List<VOTHReign>
) {
    constructor(vararg reigns: VOTHReign) : this(listOf(*reigns))

    val totalTime = reigns.map(VOTHReign::duration).fold(Duration.ZERO) { acc, duration -> acc.plus(duration) }
    val totalCost = reigns.map(VOTHReign::cost).fold(0L) { acc, cost -> acc + cost }
    val numberOfReigns = reigns.count()
}
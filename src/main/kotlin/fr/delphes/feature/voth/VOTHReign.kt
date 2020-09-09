package fr.delphes.feature.voth

import fr.delphes.User
import fr.delphes.event.incoming.RewardCost
import java.time.Duration
import java.time.LocalDateTime

data class VOTHReign(
    val voth: User,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val cost: RewardCost
) {
    val duration get() = Duration.between(start, end)
}
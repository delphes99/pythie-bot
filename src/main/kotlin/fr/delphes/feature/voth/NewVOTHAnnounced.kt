package fr.delphes.feature.voth

import fr.delphes.event.incoming.RewardRedemption
import java.time.Duration

data class NewVOTHAnnounced(
    val oldVOTH: VOTHWinner?,
    val currentVOTH: VOTHWinner,
    val rewardRedemption: RewardRedemption
) {
    val durationOfReign get() = oldVOTH?.let { old -> Duration.between(old.since, currentVOTH.since).abs() }
}
package fr.delphes.features.twitch.voth

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import java.time.Duration

data class NewVOTHAnnounced(
    val oldVOTH: VOTHWinner?,
    val currentVOTH: VOTHWinner,
    val rewardRedemption: RewardRedemption
) {
    val durationOfReign get() = oldVOTH?.let { old -> old.since?.let { Duration.between(old.since, currentVOTH.since).abs() } }
}
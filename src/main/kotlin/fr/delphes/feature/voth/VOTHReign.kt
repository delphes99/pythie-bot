@file:UseSerializers(DurationSerializer::class)

package fr.delphes.feature.voth

import fr.delphes.User
import fr.delphes.event.incoming.RewardCost
import fr.delphes.storage.serialization.DurationSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration

@Serializable
data class VOTHReign(
    val voth: User,
    val duration: Duration,
    val cost: RewardCost
)
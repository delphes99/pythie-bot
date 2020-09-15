@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.feature.voth

import fr.delphes.User
import fr.delphes.event.incoming.RewardCost
import fr.delphes.storage.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration
import java.time.LocalDateTime

@Serializable
data class VOTHReign(
    val voth: User,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val cost: RewardCost
) {
    val duration get() = Duration.between(start, end)
}
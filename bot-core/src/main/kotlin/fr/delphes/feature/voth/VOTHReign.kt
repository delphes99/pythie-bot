@file:UseSerializers(DurationSerializer::class)

package fr.delphes.feature.voth

import fr.delphes.twitch.model.User
import fr.delphes.bot.util.serialization.DurationSerializer
import fr.delphes.twitch.model.RewardCost
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration

@Serializable
data class VOTHReign(
    val voth: User,
    val duration: Duration,
    val cost: RewardCost
)
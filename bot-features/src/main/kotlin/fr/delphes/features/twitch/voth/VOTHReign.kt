@file:UseSerializers(DurationSerializer::class)

package fr.delphes.features.twitch.voth

import fr.delphes.twitch.api.user.User
import fr.delphes.utils.serialization.DurationSerializer
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardCost
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration

@Serializable
data class VOTHReign(
    val voth: User,
    val duration: Duration,
    val cost: RewardCost
)
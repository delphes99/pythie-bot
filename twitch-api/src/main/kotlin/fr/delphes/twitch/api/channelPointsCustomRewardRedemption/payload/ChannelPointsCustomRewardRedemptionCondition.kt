package fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload

import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelPointsCustomRewardRedemptionCondition(
    val broadcaster_user_id: UserId,
    val reward_id: String? = null
): GenericCondition
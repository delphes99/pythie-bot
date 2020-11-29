package fr.delphes.twitch.api.channel.channelPointsCustomRewardRedemption.payload

import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelPointsCustomRewardRedemptionCondition(
    val broadcaster_user_id: String,
    val reward_id: String? = null
): GenericCondition
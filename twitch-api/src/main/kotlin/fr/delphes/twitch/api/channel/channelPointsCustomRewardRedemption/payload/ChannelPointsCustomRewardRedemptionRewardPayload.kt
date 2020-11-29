package fr.delphes.twitch.api.channel.channelPointsCustomRewardRedemption.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelPointsCustomRewardRedemptionRewardPayload(
    val id: String,
    val title: String,
    val cost: Long,
    val prompt: String
)
package fr.delphes.twitch.api.reward.payload

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRedemptionStatus(
    val status: RedemptionStatusForUpdate
)
package fr.delphes.twitch.api.reward

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardCost
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RewardConfiguration(
    val title: String,
    val cost: RewardCost,
    val prompt: String? = null,
    val isEnabled: Boolean? = null,
    val backgroundColor: String? = null,
    val isUserInputRequired: Boolean? = null,
    val isMaxPerStreamEnabled: Boolean? = null,
    val maxPerStream: Long? = null,
    val isMaxPerUserPerStreamEnabled: Boolean? = null,
    val maxPerUserPerStream: Long? = null,
    val isGlobalCooldownEnabled: Boolean? = null,
    val globalCooldownSeconds: Long? = null,
    val shouldRedemptionsSkipRequestQueue: Boolean? = null
): WithRewardConfiguration {
    @Transient
    override val rewardConfiguration = this

    fun match(rewardId: RewardId): Boolean {
        return title == rewardId.name
    }
}
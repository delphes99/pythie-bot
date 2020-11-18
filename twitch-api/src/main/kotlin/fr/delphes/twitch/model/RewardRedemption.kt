package fr.delphes.twitch.model

data class RewardRedemption(
    val feature: Feature,
    val user: User,
    val cost: RewardCost
)

typealias RewardCost = Long

data class Feature(
    val rewardId: String,
    val rewardTitle: String,
) {
    fun isEquals(featureName: String): Boolean {
        return rewardId == featureName || rewardTitle == featureName
    }
}
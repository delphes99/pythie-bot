package fr.delphes.twitch.api.reward

data class Reward(
    val rewardId: String,
    val rewardTitle: String,
) {
    fun isEquals(rewardName: String): Boolean {
        return rewardId == rewardName || rewardTitle == rewardName
    }
}
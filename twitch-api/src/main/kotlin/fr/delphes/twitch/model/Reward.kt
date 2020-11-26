package fr.delphes.twitch.model

data class Reward(
    val rewardId: String,
    val rewardTitle: String,
) {
    fun isEquals(rewardName: String): Boolean {
        return rewardId == rewardName || rewardTitle == rewardName
    }
}
package fr.delphes.twitch.api.reward

data class Reward(
    val id: String,
    override val rewardConfiguration: RewardConfiguration
) : WithRewardConfiguration
package fr.delphes.connector.twitch.reward

data class NotDeployedReward(
    override val id: RewardId,
    val rewardConfiguration: RewardConfiguration,
) : RunTimeReward()
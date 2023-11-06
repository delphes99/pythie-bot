package fr.delphes.connector.twitch.reward

data class SynchronizedReward(
    override val id: RewardId,
    val rewardConfiguration: RewardConfiguration,
) : RunTimeReward()

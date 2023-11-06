package fr.delphes.connector.twitch.reward

data class UnmanagedReward(
    override val id: RewardId,
    val rewardConfiguration: RewardConfiguration,
) : RunTimeReward()

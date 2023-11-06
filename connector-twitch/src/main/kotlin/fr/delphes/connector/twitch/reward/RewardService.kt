package fr.delphes.connector.twitch.reward

import fr.delphes.connector.twitch.api.TwitchApi

class RewardService(
    private val twitchApi: TwitchApi,
    private val configuredRewards: ConfiguredRewards,
) {
    suspend fun getReward(rewardId: RewardId): RunTimeReward {
        val twitchMatchingReward = twitchApi.getCustomRewards(rewardId)
        val configuredMatchingReward = configuredRewards.get(rewardId)

        return when {
            twitchMatchingReward != null && configuredMatchingReward != null -> SynchronizedReward(
                rewardId, configuredMatchingReward.configuration
            )

            twitchMatchingReward != null -> UnmanagedReward(
                rewardId, twitchMatchingReward.rewardConfiguration
            )

            configuredMatchingReward != null -> NotDeployedReward(rewardId, configuredMatchingReward)
            else -> UnknownReward(rewardId)
        }
    }

    suspend fun synchronizeRewards() {
        configuredRewards.rewards.forEach { configuredReward ->
            val rewardId = configuredReward.id
            val reward = getReward(rewardId)

            when (reward) {
                is NotDeployedReward, is SynchronizedReward -> twitchApi.createOrUpdateCustomReward(configuredReward)
                is UnknownReward, is UnmanagedReward -> throw IllegalStateException("Reward must be known")
            }
        }
    }
}


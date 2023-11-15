package fr.delphes.connector.twitch.reward

import fr.delphes.connector.twitch.api.TwitchApi
import mu.KotlinLogging

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
                rewardId, twitchMatchingReward.configuration
            )

            configuredMatchingReward != null -> NotDeployedReward(
                rewardId,
                configuredMatchingReward.configuration
            )

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

    suspend fun activateReward(id: RewardId) = id.ifManaged { twitchApi.activateReward(id) }

    suspend fun deactivateReward(id: RewardId) = id.ifManaged { twitchApi.deactivateReward(id) }

    private suspend fun RewardId.ifManaged(doStuff: suspend () -> Unit) {
        when (getReward(this)) {
            is NotDeployedReward -> doStuff()
            is SynchronizedReward -> doStuff()
            is UnknownReward -> logger.warn { "Reward $this not found" }
            is UnmanagedReward -> logger.warn { "Reward $this is not managed by the bot" }
        }
    }
}

private val logger = KotlinLogging.logger {}

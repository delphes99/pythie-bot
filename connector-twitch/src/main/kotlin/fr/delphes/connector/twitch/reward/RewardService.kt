package fr.delphes.connector.twitch.reward

import fr.delphes.connector.twitch.api.TwitchApi
import io.github.oshai.kotlinlogging.KotlinLogging

class RewardService(
    private val twitchApi: TwitchApi,
    private val configuredRewards: ConfiguredRewards,
) {
    suspend fun getReward(rewardId: RewardId): RunTimeReward {
        val twitchMatchingReward = twitchApi.getCustomRewards(rewardId)
        val configuredMatchingReward = configuredRewards.get(rewardId)

        return rewardId.mapToRunTime(twitchMatchingReward, configuredMatchingReward)
    }

    private fun RewardId.mapToRunTime(
        twitchMatchingReward: InTwitchReward?,
        configuredMatchingReward: ConfiguredReward?,
    ) = when {
        twitchMatchingReward != null && configuredMatchingReward != null -> SynchronizedReward(
            this, configuredMatchingReward.configuration
        )

        twitchMatchingReward != null -> UnmanagedReward(
            this, twitchMatchingReward.configuration
        )

        configuredMatchingReward != null -> NotDeployedReward(
            this,
            configuredMatchingReward.configuration
        )

        else -> UnknownReward(this)
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

    suspend fun getRewards(): List<RunTimeReward> {
        val twitchRewards = twitchApi.getRewards()
        val ids = configuredRewards.rewards.map { it.id }.plus(twitchRewards.map { it.rewardId }).toSet()
        return ids.map { id ->
            id.mapToRunTime(
                twitchRewards.firstOrNull { it.rewardId == id },
                configuredRewards.get(id)
            )
        }
    }
}

private val logger = KotlinLogging.logger {}

package fr.delphes.twitch

import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.payload.CreateCustomReward
import fr.delphes.twitch.api.reward.payload.getCustomReward.GetCustomRewardDataPayload
import kotlinx.coroutines.runBlocking

class RewardCache(
    private val configurations: List<RewardConfiguration>,
    private val api: ChannelHelixApi,
    private val userId: String
) {
    private val map = mutableMapOf<RewardConfiguration, Reward>()

    // Move to synchronize method
    init {
        runBlocking {
            val rewards = api.getCustomRewards(userId)

            configurations.forEach { configuration ->
                val reward = rewards.find { reward -> reward.title == configuration.title }

                if (reward != null) {
                    map[configuration] = reward.toReward(configuration)

                    //TODO synchronize reward with configuration
                } else {
                    val createdReward = api.createCustomReward(
                        CreateCustomReward(
                            title = configuration.title,
                            cost = configuration.cost,
                            prompt = configuration.prompt,
                            is_enabled = true, //TODO
                            background_color = configuration.backgroundColor,
                            is_user_input_required = configuration.isUserInputRequired,
                            is_max_per_stream_enabled = configuration.isMaxPerStreamEnabled,
                            max_per_stream = configuration.maxPerStream,
                            is_max_per_user_per_stream_enabled = configuration.isMaxPerUserPerStreamEnabled,
                            max_per_user_per_stream = configuration.maxPerUserPerStream,
                            is_global_cooldown_enabled = configuration.isGlobalCooldownEnabled,
                            global_cooldown_seconds = configuration.globalCooldownSeconds,
                            should_redemptions_skip_request_queue = configuration.shouldRedemptionsSkipRequestQueue
                        ),
                        userId
                    )

                    map[configuration] = createdReward.toReward(configuration)
                }
            }
        }
    }

    fun rewardOf(configuration: RewardConfiguration): Reward? {
        return map[configuration]
    }

    private fun GetCustomRewardDataPayload.toReward(configuration: RewardConfiguration): Reward {
        return Reward(id, configuration)
    }
}


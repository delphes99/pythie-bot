package fr.delphes.twitch

import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.payload.CreateCustomReward
import fr.delphes.twitch.api.reward.payload.UpdateCustomReward
import fr.delphes.twitch.api.reward.payload.getCustomReward.GetCustomRewardDataPayload
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class RewardCache(
    private val configurations: List<RewardConfiguration>,
    private val api: ChannelHelixApi,
) {
    private val map = mutableMapOf<RewardConfiguration, Reward>()

    //TODO Move to synchronize method
    init {
        runBlocking {
            //TODO manage channels without channel points
            val rewards = try {
                api.getCustomRewards()
            } catch (e: Exception) {
                LOGGER.error(e) { "error retrieving channel point rewards" }
                emptyList()
            }

            configurations.forEach { configuration ->
                val reward = rewards.find { reward -> reward.title == configuration.title }

                if (reward != null) {
                    map[configuration] = reward.toReward(configuration)

                    if (reward.isDesynchronized(configuration)) {
                        api.updateCustomReward(
                            UpdateCustomReward(
                                title = configuration.title,
                                cost = configuration.cost,
                                prompt = configuration.prompt,
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
                            reward.id
                        )
                    }
                } else {
                    val createdReward = api.createCustomReward(
                        CreateCustomReward(
                            title = configuration.title,
                            cost = configuration.cost,
                            prompt = configuration.prompt,
                            is_enabled = true,
                            background_color = configuration.backgroundColor,
                            is_user_input_required = configuration.isUserInputRequired,
                            is_max_per_stream_enabled = configuration.isMaxPerStreamEnabled,
                            max_per_stream = configuration.maxPerStream,
                            is_max_per_user_per_stream_enabled = configuration.isMaxPerUserPerStreamEnabled,
                            max_per_user_per_stream = configuration.maxPerUserPerStream,
                            is_global_cooldown_enabled = configuration.isGlobalCooldownEnabled,
                            global_cooldown_seconds = configuration.globalCooldownSeconds,
                            should_redemptions_skip_request_queue = configuration.shouldRedemptionsSkipRequestQueue
                        )
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

    private fun GetCustomRewardDataPayload.isDesynchronized(configuration: RewardConfiguration): Boolean {
        return title != configuration.title ||
                cost != configuration.cost ||
                configuration.prompt != null && prompt != configuration.prompt ||
                configuration.backgroundColor != null && background_color != configuration.backgroundColor ||
                configuration.isUserInputRequired != null && is_user_input_required != configuration.isUserInputRequired ||
                configuration.isMaxPerStreamEnabled != null && max_per_stream_setting.is_enabled != configuration.isMaxPerStreamEnabled ||
                configuration.maxPerStream != null && max_per_stream_setting.max_per_stream != configuration.maxPerStream ||
                configuration.isMaxPerUserPerStreamEnabled != null && max_per_user_per_stream_setting.is_enabled != configuration.isMaxPerUserPerStreamEnabled ||
                configuration.maxPerUserPerStream != null && max_per_user_per_stream_setting.max_per_user_per_stream != configuration.maxPerUserPerStream ||
                configuration.isGlobalCooldownEnabled != null && global_cooldown_setting.is_enabled != configuration.isGlobalCooldownEnabled ||
                configuration.globalCooldownSeconds != null && global_cooldown_setting.global_cooldown_seconds != configuration.globalCooldownSeconds ||
                configuration.shouldRedemptionsSkipRequestQueue != null && should_redemptions_skip_request_queue != configuration.shouldRedemptionsSkipRequestQueue
    }

    fun getRewards(): List<RewardConfiguration> {
        return map.keys.toList()
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}


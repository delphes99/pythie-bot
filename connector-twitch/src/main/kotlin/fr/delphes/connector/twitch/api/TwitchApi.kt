package fr.delphes.connector.twitch.api

import fr.delphes.connector.twitch.ConfigurationTwitchAccountName
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.reward.ConfiguredReward
import fr.delphes.connector.twitch.reward.InTwitchReward
import fr.delphes.connector.twitch.reward.RewardConfiguration
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.payload.CreateCustomReward
import fr.delphes.twitch.api.reward.payload.UpdateCustomReward
import fr.delphes.twitch.api.reward.payload.getCustomReward.GetCustomRewardDataPayload
import fr.delphes.utils.flatMapNotNull

class TwitchApi(
    private val connector: TwitchConnector,
) {
    suspend fun getCustomRewards(rewardId: RewardId): InTwitchReward? {
        return rewardId.ifFound { payload ->
            payload
                ?.toRewardConfiguration()
                ?.let { InTwitchReward(rewardId, it) }
        }
    }

    suspend fun getRewards(): List<InTwitchReward> {
        return connector.configuration?.listenedChannels?.flatMapNotNull { channel ->
            connector.connectionManager.whenConnected(ConfigurationTwitchAccountName(channel.channel.name)) {
                channelTwitchApi.getRewards()
                    .map { InTwitchReward(it.toRewardId(), it.toRewardConfiguration()) }
            }
        } ?: emptyList()
    }

    suspend fun activateReward(id: RewardId) {
        id.ifFound { payload ->
            payload?.also {
                logger.debug { "Activate reward $id" }
                channelTwitchApi.activateReward(it.id)
            } ?: logger.warn { "Reward $id not found" }
        }
    }

    suspend fun deactivateReward(id: RewardId) {
        id.ifFound { payload ->
            if (payload != null) {
                logger.debug { "Deactivate reward $id" }
                channelTwitchApi.deactivateReward(payload.id)
            } else {
                logger.warn { "Reward $id not found" }
            }
        }
    }

    suspend fun <T> RewardId.ifFound(doStuff: suspend TwitchApiChannelRuntime.(GetCustomRewardDataPayload?) -> T?): T? {
        return connector.connectionManager.whenConnected(ConfigurationTwitchAccountName(channel.name)) {
            channelTwitchApi.getRewards()
                .firstOrNull { it.toRewardId() == this@ifFound }
                .let { twitchReward -> doStuff(twitchReward) }
        }
    }

    private fun GetCustomRewardDataPayload.toRewardId() = RewardId(
        TwitchChannel(broadcaster_name), RewardTitle(title)
    )

    private fun GetCustomRewardDataPayload.toRewardConfiguration(): RewardConfiguration {
        return RewardConfiguration(
            cost = cost,
            prompt = prompt,
            isEnabled = is_enabled,
            backgroundColor = background_color,
            isUserInputRequired = is_user_input_required,
            isMaxPerStreamEnabled = max_per_stream_setting.is_enabled,
            maxPerStream = max_per_stream_setting.max_per_stream,
            isMaxPerUserPerStreamEnabled = max_per_user_per_stream_setting.is_enabled,
            maxPerUserPerStream = max_per_user_per_stream_setting.max_per_user_per_stream,
            isGlobalCooldownEnabled = global_cooldown_setting.is_enabled,
            globalCooldownSeconds = global_cooldown_setting.global_cooldown_seconds,
            shouldRedemptionsSkipRequestQueue = should_redemptions_skip_request_queue
        )
    }

    suspend fun createOrUpdateCustomReward(reward: ConfiguredReward) {
        connector.connectionManager.whenConnected(ConfigurationTwitchAccountName(reward.id.channel.name)) {
            val twitchReward = channelTwitchApi.getRewards()
                .filter { it.toRewardId() == reward.id }
                .firstOrNull()

            if (twitchReward != null) {
                logger.info { "Update reward ${reward.id.title}" }
                channelTwitchApi.updateReward(reward.toUpdateCustomReward(), twitchReward.id)
            } else {
                logger.info { "Create reward ${reward.id.title}" }
                channelTwitchApi.createReward(reward.toCreateCustomReward())
            }
        }
    }

    private fun ConfiguredReward.toUpdateCustomReward(): UpdateCustomReward {
        return with(this.configuration) {
            UpdateCustomReward(
                prompt = prompt,
                cost = cost,
                background_color = backgroundColor,
                is_enabled = isEnabled,
                is_user_input_required = isUserInputRequired,
                is_max_per_stream_enabled = isMaxPerStreamEnabled,
                max_per_stream = maxPerStream,
                is_max_per_user_per_stream_enabled = isMaxPerUserPerStreamEnabled,
                max_per_user_per_stream = maxPerUserPerStream,
                is_global_cooldown_enabled = isGlobalCooldownEnabled,
                global_cooldown_seconds = globalCooldownSeconds,
                //TODO ?
                is_paused = null,
                should_redemptions_skip_request_queue = shouldRedemptionsSkipRequestQueue,
            )
        }
    }

    private fun ConfiguredReward.toCreateCustomReward(): CreateCustomReward {
        return with(this.configuration) {
            CreateCustomReward(
                title = id.title.title,
                cost = cost,
                prompt = prompt,
                is_enabled = isEnabled,
                background_color = backgroundColor,
                is_user_input_required = isUserInputRequired,
                is_max_per_stream_enabled = isMaxPerStreamEnabled,
                max_per_stream = maxPerStream,
                is_max_per_user_per_stream_enabled = isMaxPerUserPerStreamEnabled,
                max_per_user_per_stream = maxPerUserPerStream,
                is_global_cooldown_enabled = isGlobalCooldownEnabled,
                global_cooldown_seconds = globalCooldownSeconds,
                should_redemptions_skip_request_queue = shouldRedemptionsSkipRequestQueue,
            )
        }
    }
}

private val logger = mu.KotlinLogging.logger {}

package fr.delphes.twitch.api.reward.payload.getCustomReward

import kotlinx.serialization.Serializable

@Serializable
data class GetCustomRewardDataPayload(
    val id: String,
    val title: String,
    val broadcaster_name: String,
    val broadcaster_id: String,
    val image: ImagePayload? = null,
    val default_image: ImagePayload? = null,
    val background_color: String,
    val is_enabled: Boolean,
    val cost: Long,
    val prompt: String,
    val is_user_input_required: Boolean,
    val max_per_stream_setting: MaxPerStreamPayload,
    val max_per_user_per_stream_setting: MaxPerUserPerStreamPayload,
    val global_cooldown_setting: GlobalCooldownSetting,
    val is_paused: Boolean,
    val is_in_stock: Boolean,
    val should_redemptions_skip_request_queue: Boolean
)
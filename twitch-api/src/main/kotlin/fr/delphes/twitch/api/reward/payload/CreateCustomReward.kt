package fr.delphes.twitch.api.reward.payload

import kotlinx.serialization.Serializable

@Serializable
data class CreateCustomReward(
    val title: String,
    val cost: Long,
    val prompt: String? = null,
    val is_enabled: Boolean? = null,
    val background_color: String? = null,
    val is_user_input_required: Boolean? = null,
    val is_max_per_stream_enabled: Boolean? = null,
    val max_per_stream: Long? = null,
    val is_max_per_user_per_stream_enabled: Boolean? = null,
    val max_per_user_per_stream: Long? = null,
    val is_global_cooldown_enabled: Boolean? = null,
    val global_cooldown_seconds: Long? = null,
    val should_redemptions_skip_request_queue: Boolean? = null
)
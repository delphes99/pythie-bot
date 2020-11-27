package fr.delphes.twitch.api.reward.payload.rewardRedeemed

import kotlinx.serialization.Serializable

@Serializable
data class RewardPayload(
    val id: String,
    val channel_id: String,
    val title: String,
    val prompt: String,
    val cost: Long,
    val is_user_input_required: Boolean,
    val is_sub_only: Boolean,
    val image: String?,
    val default_image: DefaultImagePayload,
    val background_color: String,
    val is_enabled: Boolean,
    val is_paused: Boolean,
    val is_in_stock: Boolean,
    val should_redemptions_skip_request_queue: Boolean,
    val template_id: String?
)
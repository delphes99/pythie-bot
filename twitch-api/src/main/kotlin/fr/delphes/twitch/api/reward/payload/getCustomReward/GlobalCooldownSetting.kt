package fr.delphes.twitch.api.reward.payload.getCustomReward

import kotlinx.serialization.Serializable

@Serializable
data class GlobalCooldownSetting(
    val is_enabled: Boolean,
    val global_cooldown_seconds: Long
)
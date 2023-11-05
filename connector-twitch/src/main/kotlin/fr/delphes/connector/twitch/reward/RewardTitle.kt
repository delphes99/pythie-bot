package fr.delphes.connector.twitch.reward

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class RewardTitle(val title: String)
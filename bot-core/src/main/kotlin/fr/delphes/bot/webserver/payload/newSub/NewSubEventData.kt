package fr.delphes.bot.webserver.payload.newSub

import kotlinx.serialization.Serializable

@Serializable
data class NewSubEventData(
    val broadcaster_id: String,
    val broadcaster_name: String,
    val is_gift: Boolean,
    val plan_name: String,
    val tier: String,
    val user_id: String,
    val user_name: String,
    val gifter_id: String,
    val gifter_name: String
)

package fr.delphes.bot.webserver.payload

import kotlinx.serialization.Serializable

@Serializable
data class NewFollowPayloadData(
    val from_id: String,
    val from_name: String,
    val to_id: String,
    val to_name: String,
    val followed_at: String
)
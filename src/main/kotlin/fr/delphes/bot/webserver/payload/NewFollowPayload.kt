package fr.delphes.bot.webserver.payload

import kotlinx.serialization.Serializable

@Serializable
data class NewFollowPayload(
    val data: List<NewFollowPayloadData>
)
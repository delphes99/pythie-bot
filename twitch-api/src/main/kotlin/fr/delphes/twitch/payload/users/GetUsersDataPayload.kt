package fr.delphes.twitch.payload.users

import kotlinx.serialization.Serializable

@Serializable
data class GetUsersDataPayload(
    val id: String,
    val login: String,
    val display_name: String,
    val type: String,
    val broadcaster_type: String,
    val description: String,
    val profile_image_url: String,
    val offline_image_url: String,
    val view_count: Long,
    val email: String? = null
)
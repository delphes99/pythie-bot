package fr.delphes.twitch.api.user.payload

import fr.delphes.twitch.api.user.UserId
import kotlinx.serialization.Serializable

@Serializable
data class GetUsersDataPayload(
    val id: UserId,
    val login: String,
    val display_name: String,
    val type: String,
    val broadcaster_type: BroadcasterType,
    val description: String,
    val profile_image_url: String,
    val offline_image_url: String,
    val view_count: Long,
    val email: String? = null
)
package fr.delphes.twitch.api.user.payload

import kotlinx.serialization.Serializable

@Serializable
data class GetUsersPayload(
    val data: List<GetUsersDataPayload>
)
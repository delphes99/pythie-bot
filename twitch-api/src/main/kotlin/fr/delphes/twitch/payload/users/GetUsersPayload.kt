package fr.delphes.twitch.payload.users

import kotlinx.serialization.Serializable

@Serializable
data class GetUsersPayload(
    val data: List<GetUsersDataPayload>
)
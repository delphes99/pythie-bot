package fr.delphes.twitch.api.clips.payload

import kotlinx.serialization.Serializable

@Serializable
data class GetClips(
    val data: List<GetClipsPayload>
)

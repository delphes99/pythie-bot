package fr.delphes.twitch.api.user

import fr.delphes.twitch.api.user.payload.BroadcasterType

data class TwitchUser(
    val id: String,
    val name: String,
    val broadcasterType: BroadcasterType,
    val viewCount: Long
)
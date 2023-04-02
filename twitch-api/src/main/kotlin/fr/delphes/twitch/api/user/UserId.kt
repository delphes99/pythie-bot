package fr.delphes.twitch.api.user

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class UserId(val id: String)
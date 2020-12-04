package fr.delphes.bot.state

import fr.delphes.twitch.api.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UserCheer(
    val user: User?,
    val bits: Long
)
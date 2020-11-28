package fr.delphes.twitch.api.newFollow

import fr.delphes.twitch.api.user.User
import java.time.LocalDateTime

data class NewFollow(
    val follower: User,
    val at: LocalDateTime
)
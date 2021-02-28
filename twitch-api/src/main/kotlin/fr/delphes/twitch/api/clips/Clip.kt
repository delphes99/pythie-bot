package fr.delphes.twitch.api.clips

import fr.delphes.twitch.api.streams.ThumbnailUrl
import fr.delphes.twitch.api.user.User
import java.time.LocalDateTime

data class Clip(
    val url: String,
    val creator: User,
    val gameId: String,
    val title: String,
    val createdAt: LocalDateTime,
    val thumbnailUrl: ThumbnailUrl
)
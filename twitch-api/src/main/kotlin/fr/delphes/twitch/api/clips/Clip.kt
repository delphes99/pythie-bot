package fr.delphes.twitch.api.clips

import fr.delphes.twitch.api.streams.ThumbnailUrl
import fr.delphes.twitch.api.user.UserName
import java.time.LocalDateTime

data class Clip(
    val url: String,
    val creator: UserName,
    val gameId: String,
    val title: String,
    val createdAt: LocalDateTime,
    val thumbnailUrl: ThumbnailUrl
)
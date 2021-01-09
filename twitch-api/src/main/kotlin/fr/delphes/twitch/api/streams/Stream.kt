package fr.delphes.twitch.api.streams

import fr.delphes.twitch.api.games.Game
import java.time.LocalDateTime

data class Stream(
    val id: String,
    val title: String,
    val start: LocalDateTime,
    val game: Game,
    val thumbnailUrl: ThumbnailUrl
) {
    constructor(
        id: String,
        title: String,
        start: LocalDateTime,
        game: Game,
        thumbnailUrl: String
    ) : this(
        id,
        title,
        start,
        game,
        ThumbnailUrl(thumbnailUrl)
    )
}
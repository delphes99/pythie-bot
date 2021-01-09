package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.streams.ThumbnailUrl
import java.time.LocalDateTime

data class StreamOnline(
    val title: String,
    val start: LocalDateTime,
    val game: Game,
    val thumbnailUrl: ThumbnailUrl
) : IncomingEvent {
    constructor(
        title: String,
        start: LocalDateTime,
        game: Game,
        thumbnailUrl: String
    ) : this(
        title,
        start,
        game,
        ThumbnailUrl(thumbnailUrl)
    )
}
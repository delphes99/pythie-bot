package fr.delphes.bot.event.incoming

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.streams.ThumbnailUrl
import java.time.LocalDateTime

data class StreamOnline(
    override val channel: TwitchChannel,
    val title: String,
    val start: LocalDateTime,
    val game: Game,
    val thumbnailUrl: ThumbnailUrl
) : TwitchIncomingEvent {
    constructor(
        channel: TwitchChannel,
        title: String,
        start: LocalDateTime,
        game: Game,
        thumbnailUrl: String
    ) : this(
        channel,
        title,
        start,
        game,
        ThumbnailUrl(thumbnailUrl)
    )
}
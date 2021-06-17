package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.streams.ThumbnailUrl
import java.time.LocalDateTime

data class StreamOnline(
    override val channel: TwitchChannel,
    val id: String,
    val title: String,
    val start: LocalDateTime,
    val game: Game,
    val thumbnailUrl: ThumbnailUrl
) : TwitchIncomingEvent {
    constructor(
        channel: TwitchChannel,
        id: String,
        title: String,
        start: LocalDateTime,
        game: Game,
        thumbnailUrl: String
    ) : this(
        channel,
        id,
        title,
        start,
        game,
        ThumbnailUrl(thumbnailUrl)
    )
}
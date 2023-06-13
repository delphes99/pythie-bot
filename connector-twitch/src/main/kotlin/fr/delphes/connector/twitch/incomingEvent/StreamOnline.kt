@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.annotation.incomingEvent.RegisterIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.streams.ThumbnailUrl
import fr.delphes.utils.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
@RegisterIncomingEvent
data class StreamOnline(
    override val channel: TwitchChannel,
    val id: String,
    val title: String,
    val start: LocalDateTime,
    val game: Game?,
    val thumbnailUrl: ThumbnailUrl,
) : TwitchIncomingEvent {
    constructor(
        channel: TwitchChannel,
        id: String,
        title: String,
        start: LocalDateTime,
        game: Game?,
        thumbnailUrl: String,
    ) : this(
        channel,
        id,
        title,
        start,
        game,
        ThumbnailUrl(thumbnailUrl)
    )
}
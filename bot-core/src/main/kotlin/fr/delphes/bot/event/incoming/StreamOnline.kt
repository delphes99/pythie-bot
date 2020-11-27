package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.games.Game
import java.time.LocalDateTime

data class StreamOnline(
    val title: String,
    val start: LocalDateTime,
    val game: Game
) : IncomingEvent
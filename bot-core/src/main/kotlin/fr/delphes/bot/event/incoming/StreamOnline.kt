package fr.delphes.bot.event.incoming

import fr.delphes.bot.twitch.game.Game
import java.time.LocalDateTime

data class StreamOnline(
    val title: String,
    val start: LocalDateTime,
    val game: Game
) : IncomingEvent
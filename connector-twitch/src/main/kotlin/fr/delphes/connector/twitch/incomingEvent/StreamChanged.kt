package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.Serializable

@Serializable
data class StreamChanged(
    override val channel: TwitchChannel,
    val changes: List<StreamChanges>,
) : TwitchIncomingEvent {
    constructor(
        channel: TwitchChannel,
        vararg changes: StreamChanges,
    ) : this(channel, listOf(*changes))
}

@Serializable
sealed class StreamChanges {
    data class Title(
        val oldTitle: String,
        val newTitle: String,
    ) : StreamChanges()

    data class Game(
        val oldGame: fr.delphes.twitch.api.games.Game?,
        val newGame: fr.delphes.twitch.api.games.Game?,
    ) : StreamChanges()
}
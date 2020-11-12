package fr.delphes.bot.event.incoming

data class StreamChanged(
    val changes: List<StreamChanges>
) : IncomingEvent {
    constructor(vararg changes: StreamChanges) : this(listOf(*changes))
}

sealed class StreamChanges {
    data class Title(
        val oldTitle: String,
        val newTitle: String
    ) : StreamChanges()

    data class Game(
        val oldGame: fr.delphes.bot.twitch.game.Game,
        val newGame: fr.delphes.bot.twitch.game.Game
    ) : StreamChanges()
}
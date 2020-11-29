package fr.delphes.twitch.api.games

interface WithGameId {
    val id: String

    val gameId get() = GameId(id)
}
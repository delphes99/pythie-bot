package fr.delphes.bot.twitch.game

class TwitchGameRepository(
    private val getGame: (GameId) -> Game
): GameRepository {
    override fun get(gameId: GameId): Game {
        return getGame(gameId)
    }
}
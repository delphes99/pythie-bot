package fr.delphes.bot.twitch.game

class TwitchGameRepository(
    private val getGame: (String) -> Game
): GameRepository {
    override fun get(id: String): Game {
        return getGame(id)
    }
}
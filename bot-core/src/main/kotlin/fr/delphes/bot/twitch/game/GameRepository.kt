package fr.delphes.bot.twitch.game

interface GameRepository {
    fun get(gameId: GameId) : Game
}
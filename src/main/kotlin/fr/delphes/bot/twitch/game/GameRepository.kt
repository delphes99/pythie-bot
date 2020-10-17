package fr.delphes.bot.twitch.game

interface GameRepository {
    fun get(id: String) : Game
}
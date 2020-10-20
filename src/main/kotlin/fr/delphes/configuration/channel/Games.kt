package fr.delphes.configuration.channel

import fr.delphes.bot.twitch.game.GameId

enum class Games(override val id: String) : GameId {
    SCIENCE_TECHNOLOGY("509670"),
    JUST_CHATTING("509658"),
    SATISFACTORY("506456");
}
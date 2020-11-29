package fr.delphes.twitch.api.channelUpdate

import fr.delphes.twitch.api.games.Game

data class ChannelUpdate(
    val title: String,
    val language: String,
    val game: Game
)
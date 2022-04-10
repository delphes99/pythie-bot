package fr.delphes.twitch.api.channel

import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.user.TwitchUser

data class ChannelInformation(
    val broadcaster: TwitchUser,
    val language: String,
    val game: Game,
    val title: String,
    val delay: Int
)

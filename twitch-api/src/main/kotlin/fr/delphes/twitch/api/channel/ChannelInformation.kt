package fr.delphes.twitch.api.channel

import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.api.user.UserId

data class ChannelInformation(
    val broadcaster: UserId,
    val language: String,
    val game: Game,
    val title: String,
    val delay: Int
)

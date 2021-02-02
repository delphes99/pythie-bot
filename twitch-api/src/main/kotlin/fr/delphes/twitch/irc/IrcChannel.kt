package fr.delphes.twitch.irc

import fr.delphes.twitch.TwitchChannel

data class IrcChannel(
    val name: String
) {
    val ircName = "#$name"

    fun toTwitchChannel() = TwitchChannel(name)
}
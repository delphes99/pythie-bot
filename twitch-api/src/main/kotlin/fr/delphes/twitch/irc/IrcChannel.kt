package fr.delphes.twitch.irc

data class IrcChannel(
    val name: String
) {
    val ircName = "#$name"
}
package fr.delphes.twitch.irc

import fr.delphes.twitch.TwitchChannel

data class IrcChannel private constructor(
    val name: String
) {
    val ircName = "#${name.lowercase()}"

    fun toTwitchChannel() = TwitchChannel(name)

    companion object {
        fun withName(name: String) : IrcChannel {
            val commonName = if (name.startsWith("#")) {
                name.substring(1)
            } else {
                name
            }
            return IrcChannel(commonName)
        }

        fun of(channel: TwitchChannel) : IrcChannel {
            return withName(channel.normalizeName)
        }
    }
}
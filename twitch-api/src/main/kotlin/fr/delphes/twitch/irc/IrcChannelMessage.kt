package fr.delphes.twitch.irc

import fr.delphes.twitch.TwitchChannel

data class IrcChannelMessage(
    val channel: IrcChannel,
    val user: IrcUser,
    val message: String
) {
    fun isFor(twitchChannel: TwitchChannel): Boolean {
        //TODO normalize twitch channel name
        return channel.toTwitchChannel().name.equals(twitchChannel.name, true)
    }
}
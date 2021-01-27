package fr.delphes.twitch.irc

data class IrcChannelMessage(
    val channel: IrcChannel,
    val user: IrcUser,
    val message: String
)
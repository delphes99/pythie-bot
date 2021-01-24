package fr.delphes.twitch.irc

data class Message(
    val user: IrcUser,
    val message: String
)
package fr.delphes.event.outgoing

import com.github.twitch4j.chat.TwitchChat
import fr.delphes.User

data class PromoteVIP(val user: User) : OutgoingEvent {
    constructor(user: String) : this(User(user))

    override fun applyOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String) {
        ownerChat.sendMessage(channel, "/vip $user")
    }
}
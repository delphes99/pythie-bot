package fr.delphes.event.outgoing

import com.github.twitch4j.chat.TwitchChat
import fr.delphes.User

data class RemoveVIP(val user: User): OutgoingEvent {
    override fun applyOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String) {
        ownerChat.sendMessage(channel, "/unvip $user")
    }
}
package fr.delphes.event.outgoing

import com.github.twitch4j.chat.TwitchChat

data class SendMessage(
    val text: String
) : OutgoingEvent {
    override fun applyOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String) {
        chat.sendMessage(channel, text)
    }
}
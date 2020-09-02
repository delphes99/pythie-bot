package fr.delphes.event.outgoing

import com.github.twitch4j.chat.TwitchChat

object RetrieveVip : OutgoingEvent {
    override fun applyOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String) {
        chat.sendMessage(channel, "/vips")
    }
}
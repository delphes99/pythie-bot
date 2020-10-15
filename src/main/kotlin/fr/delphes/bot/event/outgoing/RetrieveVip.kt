package fr.delphes.bot.event.outgoing

import com.github.twitch4j.chat.TwitchChat

object RetrieveVip : OutgoingEvent {
    override fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String) {
        ownerChat.sendMessage(channel, "/vips")
    }
}
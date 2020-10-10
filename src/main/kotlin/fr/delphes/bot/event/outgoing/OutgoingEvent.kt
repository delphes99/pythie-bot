package fr.delphes.bot.event.outgoing

import com.github.twitch4j.chat.TwitchChat

interface OutgoingEvent {
    fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String)
}
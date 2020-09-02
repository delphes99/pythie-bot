package fr.delphes.event.outgoing

import com.github.twitch4j.chat.TwitchChat

interface OutgoingEvent {
    fun applyOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String)
}
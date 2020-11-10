package fr.delphes.bot.event.outgoing

import com.github.twitch4j.chat.TwitchChat
import fr.delphes.User

sealed class OutgoingEvent

data class Alert(val type: String, val parameters: Map<String, String>): OutgoingEvent() {
    constructor(type: String, vararg parameters: Pair<String, String>): this(type, mapOf(*parameters))
}

sealed class TwitchOutgoingEvent: OutgoingEvent() {
    abstract fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String)
}

data class PromoteVIP(val user: User) : TwitchOutgoingEvent() {
    constructor(user: String) : this(User(user))

    override fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String) {
        ownerChat.sendMessage(channel, "/vip $user")
    }
}

data class RemoveVIP(val user: User): TwitchOutgoingEvent() {
    constructor(user: String) : this(User(user))

    override fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String) {
        ownerChat.sendMessage(channel, "/unvip $user")
    }
}

object RetrieveVip : TwitchOutgoingEvent() {
    override fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String) {
        ownerChat.sendMessage(channel, "/vips")
    }
}

data class SendMessage(
    val text: String
) : TwitchOutgoingEvent() {
    override fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, channel: String) {
        chat.sendMessage(channel, text)
    }
}